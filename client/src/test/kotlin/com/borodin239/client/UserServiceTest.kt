package com.borodin239.client

import com.borodin239.client.controllers.UserController
import com.borodin239.client.domain.User
import com.borodin239.client.dto.AddMoneyRequestDto
import com.borodin239.client.dto.RegisterUserRequestDto
import com.borodin239.client.repository.UserRepository
import com.borodin239.common.dto.BaseResponse
import com.borodin239.common.exceptions.StocksIllegalRequestException
import com.borodin239.common.exceptions.UserNotFoundException
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.LocalDateTime


class MyPostgreSQLContainer(imageName: String) : PostgreSQLContainer<MyPostgreSQLContainer>(imageName)

@Container
private val postgres: MyPostgreSQLContainer = MyPostgreSQLContainer("postgres")
    .withDatabaseName("market")
    .withExposedPorts(5432)
    .withUsername("stocks")
    .withPassword("stocks")
    .withCreateContainerCmdModifier {
        it.withHostConfig(
            HostConfig().withPortBindings(PortBinding(Ports.Binding.bindPort(5433), ExposedPort(5432)))
        )
    }.also { it.start() }

@ComponentScan
@SpringBootTest(classes = [UserServiceTest::class])
@ContextConfiguration(initializers = [UserServiceTest.PropertiesInitializer::class])
class UserServiceTest {

    @Autowired
    lateinit var userController: UserController

    @Autowired
    lateinit var userRepository: UserRepository

    internal class PropertiesInitializer : ApplicationContextInitializer<ConfigurableApplicationContext?> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of(
                "spring.r2dbc.url=" + postgres.jdbcUrl.replace("jdbc", "r2dbc"),
                "spring.r2dbc.username=" + postgres.username,
                "spring.r2dbc.password=" + postgres.password,
                "spring.r2bdc.port=" + postgres.exposedPorts,
            ).applyTo(configurableApplicationContext.environment)
        }
    }

    @Test
    fun testRegistration() {
        val dto = RegisterUserRequestDto("TEST_REGISTRATION")
        val response = userController.registerUser(dto)
        Assertions.assertNotNull(response)
        assertEquals("TEST_REGISTRATION", response.block()?.login)
    }

    @Test
    fun testRegistrationUserAlreadyExist() {
        val dto = RegisterUserRequestDto("REPEAT_USER")
        val firstResponse = userController.registerUser(dto)
        Assertions.assertNotNull(firstResponse.block())
        val secondResponse = userController.registerUser(dto)
        Assertions.assertNotNull(secondResponse)
        Assertions.assertThrows(DataIntegrityViolationException::class.java) { secondResponse.block() }
    }

    @Test
    fun testAddMoneySuccess() {
        userRepository.save(User("ADD_MONEY", BigDecimal.ZERO, LocalDateTime.now())).subscribe()
        val dto = AddMoneyRequestDto("ADD_MONEY", BigDecimal(10))
        val response: Mono<BaseResponse> = userController.addMoneyToAccount(dto)
        Assertions.assertNotNull(response)
        assertTrue(response.block()!!.success)
        assertEquals(userRepository.findByLogin("ADD_MONEY").block()?.balance, BigDecimal(10))
    }

    @Test
    fun testAddMoneyUserNotExist() {
        val dto = AddMoneyRequestDto("NOT_EXISTS", BigDecimal(10))
        val response: Mono<BaseResponse> = userController.addMoneyToAccount(dto)
        Assertions.assertNotNull(response)
        Assertions.assertThrows(UserNotFoundException::class.java) { response.block() }
    }

    @Test
    fun testAddMoneyUserIncorrectSum() {
        val dto = AddMoneyRequestDto("INCORRECT_SUM", BigDecimal(-1))
        Assertions.assertThrows(StocksIllegalRequestException::class.java) { userController.addMoneyToAccount(dto) }
    }
}
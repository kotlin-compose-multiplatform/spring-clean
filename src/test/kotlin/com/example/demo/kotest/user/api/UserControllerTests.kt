package com.example.demo.kotest.user.api

import com.example.demo.security.SecurityUserItem
import com.example.demo.user.api.UserController
import com.example.demo.user.application.ChangeUserService
import com.example.demo.user.application.GetUserService
import com.example.demo.user.dto.serve.request.CreateUserRequest
import com.example.demo.user.dto.serve.request.UpdateUserRequest
import com.example.demo.user.dto.serve.response.CreateUserResponse.Companion.of
import com.example.demo.user.dto.serve.response.GetUserResponse
import com.example.demo.user.dto.serve.response.UpdateMeResponse
import com.example.demo.user.dto.serve.response.UpdateUserResponse
import com.example.demo.user.entity.User
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.instancio.Instancio
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Tags("kotest-unit-test")
class UserControllerTests : FunSpec({
  val userController = mockk<UserController>()
  val getUserService = mockk<GetUserService>()
  val changeUserService = mockk<ChangeUserService>()

  val user: User = Instancio.create(User::class.java)
  val defaultPageable = Pageable.ofSize(1)
  val defaultAccessToken =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n" +  //
      ""

  test("Get User By Id") {

    every { getUserService.getUserById(any<Long>()) } returns GetUserResponse.of(user)

    every {
      userController.getUserById(
        any<Long>()
      )
    } returns ResponseEntity.ok(GetUserResponse.of(user))

    val response = userController.getUserById(
      user.id
    )

    response shouldNotBeNull {
      statusCode shouldBe HttpStatus.OK
      body shouldNotBeNull {
        userId shouldBe user.id
        email shouldBe user.email
        name shouldBe user.name
        role shouldBe user.role
      }
    }
  }

  test("Get User List") {

    every { getUserService.getUserList(any<Pageable>()) } returns PageImpl(
      listOf(GetUserResponse.of(user)),
      defaultPageable,
      1
    )

    every {
      userController.getUserList(
        any<Pageable>()
      )
    } returns ResponseEntity.ok(PageImpl(listOf(GetUserResponse.of(user)), defaultPageable, 1))

    val response = userController.getUserList(
      defaultPageable
    )

    response shouldNotBeNull {
      statusCode shouldBe HttpStatus.OK
      body shouldNotBeNull {
        content[0] shouldNotBeNull {
          userId shouldBe user.id
          email shouldBe user.email
          name shouldBe user.name
          role shouldBe user.role
        }
      }
    }
  }

  test("Create User") {
    val createUserRequest = Instancio.create(
      CreateUserRequest::class.java
    )

    every { changeUserService.createUser(any<CreateUserRequest>()) } returns of(user, defaultAccessToken)

    every { userController.createUser(any<CreateUserRequest>()) } returns ResponseEntity.status(HttpStatus.CREATED)
      .body(
        of(
          user,
          defaultAccessToken
        )
      )

    val response = userController.createUser(
      createUserRequest
    )

    response shouldNotBeNull {
      statusCode shouldBe HttpStatus.CREATED
      body shouldNotBeNull {
        email shouldBe user.email
        name shouldBe user.name
        role shouldBe user.role
        accessToken shouldBe defaultAccessToken
      }
    }
  }

  test("Update User") {
    val updateUserRequest = Instancio.create(
      UpdateUserRequest::class.java
    )

    every {
      changeUserService.updateUser(
        any<Long>(),
        any<UpdateUserRequest>()
      )
    } returns UpdateUserResponse.of(user)

    every {
      userController.updateUser(
        any<UpdateUserRequest>(),
        any<Long>(),
      )
    } returns ResponseEntity.ok(UpdateUserResponse.of(user))


    val response = userController.updateUser(
      updateUserRequest,
      user.id
    )

    response shouldNotBeNull {
      statusCode shouldBe HttpStatus.OK
      body shouldNotBeNull {
        email shouldBe user.email
        name shouldBe user.name
        role shouldBe user.role
      }
    }
  }

  test("Update Me") {
    val updateUserRequest = Instancio.create(
      UpdateUserRequest::class.java
    )
    val securityUserItem = Instancio.create(
      SecurityUserItem::class.java
    )

    every { changeUserService.updateMe(any<Long>(), any<UpdateUserRequest>()) } returns UpdateMeResponse.of(
      user,
      defaultAccessToken
    )

    every { userController.updateMe(any<UpdateUserRequest>(), any<SecurityUserItem>()) } returns ResponseEntity.ok(
      UpdateMeResponse.of(
        user,
        defaultAccessToken
      )
    )

    val response = userController.updateMe(
      updateUserRequest,
      securityUserItem
    )

    response shouldNotBeNull {
      statusCode shouldBe HttpStatus.OK
      body shouldNotBeNull {
        email shouldBe user.email
        name shouldBe user.name
        role shouldBe user.role
        accessToken shouldBe defaultAccessToken
      }
    }
  }

  test("Delete User") {

    justRun { changeUserService.deleteUser(any<Long>()) }

    every { userController.deleteUser(any<Long>()) } returns ResponseEntity.noContent().build()

    val response = userController.deleteUser(user.id)

    response shouldNotBeNull {
      body.shouldBeNull()
      statusCode shouldBe HttpStatus.NO_CONTENT
    }
  }
})

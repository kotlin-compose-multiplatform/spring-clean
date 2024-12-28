package com.example.demo.kotest.utils

import com.example.demo.utils.SecurityUtils.sendErrorResponse
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Tags("kotest-unit-test")
class SecurityUtilsTest : DescribeSpec({
  val mockHttpServletRequest = mockk<MockHttpServletRequest>(relaxed = true)
  val mockHttpServletResponse = mockk<MockHttpServletResponse>(relaxed = true)

  describe("Start Security Exception") {
    val exception = Instancio.create(Throwable::class.java)

    context("Call sendErrorResponse method") {

      it("Exception response to client") {

        justRun {
          mockHttpServletResponse.status = any<Int>()
          mockHttpServletResponse.contentType = any<String>()
        }

        sendErrorResponse(
          mockHttpServletRequest,
          mockHttpServletResponse,
          exception,
          "test exception"
        )

        verify(exactly = 1) {
          sendErrorResponse(
            mockHttpServletRequest,
            mockHttpServletResponse,
            exception,
            "test exception"
          )

          mockHttpServletResponse.writer
          mockHttpServletResponse.status = HttpStatus.UNAUTHORIZED.value()
          mockHttpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
        }
      }
    }
  }
})

package com.softwaremill.macwire.aop

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class ProxyingInterceptorTest extends FlatSpec with ShouldMatchers {

  it should "proxy calls to the given object" in {
    // given
    var before = 0
    var after = 0

    val toIntercept = new SomeClass

    val interceptor = ProxyingInterceptor { ctx =>
      try {
        before += 1
        ctx.proceed()
      } finally {
        after += 1
      }
    }

    // when
    val intercepted = interceptor(toIntercept)

    intercepted.methodA()
    val bResult = intercepted.methodB(10)

    // then
    toIntercept.a should be (1)
    toIntercept.b should be (11)

    bResult should be (11)

    before should be (2)
    after should be (2)
  }

  class SomeClass {
    var a = 0
    var b = 1

    def methodA() {
      a += 1
      println("A is: " + a)
    }

    def methodB(x: Int) = {
      b += x
      b
    }
  }
}
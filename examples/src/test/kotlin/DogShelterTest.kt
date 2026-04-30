import io.kotest.core.spec.style.FunSpec

class ExampleSpec : FunSpec({



    beforeSpec {
        println("this block executes before spec")
    }

    afterSpec {
        println("this block executes after spec")
    }

    context("context 1") {
        beforeTest {
            println("this block executes before each test")
        }

        afterTest {
            println("this block executes after each test")
        }

        test("test 1") {
            println("test 1")
        }
    }


    test("test 2") {
        println("test 2")
    }

    test("test 3") {
        println("test 3")
    }
})

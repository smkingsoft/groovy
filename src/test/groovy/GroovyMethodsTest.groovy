import java.io.InputStreamReader

/** 
 * Tests the various new Groovy methods
 * 
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 * @author Guillaume Laforge
 * @version $Revision$
 */
class GroovyMethodsTest extends GroovyTestCase {
    void testCollect() {
        assert [2, 4, 6].collect { it * 2} == [4, 8, 12]

        def answer = [2, 4, 6].collect(new Vector()) { it * 2}

        assert answer[0] == 4
        assert answer[1] == 8
        assert answer[2] == 12
    }

    void testJoin() {
        assert [2, 4, 6].join("-") == "2-4-6"
        assert ["edam", "cheddar", "brie"].join(", ") == 'edam, cheddar, brie'
        
        println( ["abc", 5, 2.34].join(", ") )
    }
    
    void testTimes() {
        def count = 0
        5.times { i -> count = count + i }
        assert count == 10

        count = 0
        def temp = 5
        temp.times { i -> count = count + i }

        assert count == 10
    }

    void testArraySubscript() {
        def list = [1, 2, 3, 4]
        def array = list.toArray()

        def value = array[2]

        assert value == 3

        array[0] = 9

       assert array[0] == 9
    }

    void testToCharacterMethod() {
        def s = 'c'
        def x = s.toCharacter()

        assert x instanceof Character
    }

    void testListGrep() {
        def list = ["James", "Bob", "Guillaume", "Sam"]
        def answer = list.grep(~".*a.*")

        assert answer == ["James", "Guillaume", "Sam"]

        answer = list.grep(~"B.b")

        assert answer == ["Bob"]
    }

    void testCollectionToList() {
        def c = [1, 2, 3, 4, 5] // but it's a list
        def l = c.toList()

        assert l.containsAll(c)
        assert c.size() == l.size()
    }

    void testJoinString() {
        String[] arr = ["a", "b", "c", "d"]
        def joined = arr.join(", ")

        assert joined == "a, b, c, d"
    }

    void testReverseEach() {
        def l = ["cheese", "loves", "Guillaume"]
        def expected = ["Guillaume", "loves", "cheese"]

        def answer = []
        l.reverseEach{ answer << it }

        assert answer == expected
    }

    void testGrep() {
        def list = ["Guillaume", "loves", "cheese"]

        def answer = list.grep(~".*ee.*")
        assert answer == ["cheese"]

        list = [123, "abc", 4.56]
        answer = list.grep(String)
        assert answer == ["abc"]

        list = [4, 2, 7, 3, 6, 2]
        answer = list.grep(2..3)
        assert answer == [2, 3, 2]
    }

    void testMapGetWithDefault() {
        def map = [:]

        assert map.foo == null

        map.get("foo", []).add(123)

        assert map.foo == [123]

        map.get("bar", [:]).get("xyz", [:]).cheese = 123

        assert map.bar.xyz.cheese == 123
        assert map.size() == 2
    }

    void DISABLE_testExecuteCommandLineProcessUsingAString() {
        /** @todo why does this not work
        javaHome = System.getProperty('java.home', '')
        cmd = "${javaHome}/bin/java -version"
        */

        def cmd = "ls -l"
        if (System.getProperty('os.name', '').contains('Win')) {
            cmd = "dir"
        }

        println "executing command: ${cmd}"

        def process = cmd.execute()
        //process = "ls -l".execute()

        // lets have an easier way to do this!
        def count = 0

        println "Read the following lines..."

        /** @todo we should simplify the following line!!! */
        new InputStreamReader(process.in).eachLine { line ->
            println line
            ++count
        }
        println ""

        process.waitFor()
        def value = process.exitValue()
        println "Exit value of command line is ${value}"

        assert count > 1
    }
    
    void DISABLED_testExecuteCommandLineProcessAndUseWaitForOrKill() {
        /** @todo why does this not work
        javaHome = System.getProperty('java.home', '')
        cmd = "${javaHome}/bin/java -version"
        */

        def cmd = "ls -l"
        if (System.getProperty('os.name', '').contains('Win')) {
            cmd = "dir"
        }

        println "executing command: ${cmd}"

        def process = cmd.execute()

        process.waitForOrKill(1000)
        def value = process.exitValue()
        println "Exit value of command line is ${value}"


        process = cmd.execute()

        process.waitForOrKill(1)
        value = process.exitValue()
        println "Exit value of command line is ${value}"
    }
    
    void DISABLE_testExecuteCommandLineUnderWorkingDirectory() {
        def cmd = "ls -l"
        if (System.getProperty('os.name', '').contains('Win')) {
            cmd = "dir"
        }

        def envp = java.util.Array.newInstance(String, 0)
        def workDir = new File(".")

        println "executing command: ${cmd} under the directory ${workDir}"

        def process = cmd.execute(envp, workDir)

        // lets have an easier way to do this!
        def count = 0

        println "Read the following lines under the directory ${workDir} ..."

        /** @todo we should simplify the following line!!! */
        new InputStreamReader(process.in).eachLine { line ->
            println line
            ++count
        }
        println ""

        process.waitFor()
        def value = process.exitValue()
        println "Exit value of command line is ${value}"

        assert count > 1
    }
    
    void testDisplaySystemProperties() {
        println "System properties are..."
        def properties = System.properties
        def keys = properties.keySet().sort()
        for (k in keys) {
            println "${k} = ${properties[k]}"
        }
    }

    void testMax() {
        assert [-5, -3, -1, 0, 2, 4].max{ it * it } == -5
    }

    void testMin() {
        assert [-5, -3, -1, 0, 2, 4].min{ it * it } == 0
    }
    
    void testSort() {
        assert [-5, -3, -1, 0, 2, 4].sort { it*it } == [0, -1, 2, -3, 4, -5]
    }

    void testReplaceAllClosure() {
        assert "1 a 2 b 3 c 4".replaceAll("\\p{Digit}") { it * 2 } == "11 a 22 b 33 c 44"
    }

    void testObjectSleep(){
        long start = System.currentTimeMillis()
        sleep 1
        long slept = System.currentTimeMillis() - start
        assertTrue("should have slept >= 1s but was ${slept}ms", slept >= 1000)
    }

    void testObjectSleepInterrupted(){
        def interruptor = new groovy.TestInterruptor(Thread.currentThread())
        new Thread(interruptor).start()
        long start = System.currentTimeMillis()
        sleep 1
        long slept = System.currentTimeMillis() - start
        long epsilon = 100
        assertTrue("should have slept >= 1s but was ${slept}ms", slept >= 1000-epsilon)
    }
    void testObjectSleepWithOnInterruptHandler(){
        def log = ''
        def interruptor = new groovy.TestInterruptor(Thread.currentThread())
        new Thread(interruptor).start()
        long start = System.currentTimeMillis()
        sleep(1){ log += it.toString() }
        long slept = System.currentTimeMillis() - start
        assert slept < 1000, "should have been interrupted but slept ${slept}ms > 2s"
        assertEquals 'java.lang.InterruptedException: sleep interrupted', log.toString()
    }

    void testObjectIdentity() {
        def a = new Object()
        def b = a
        assert a.is(b)
        assert ! a.is(null)
        assert ! 1.is(2)
        // naive impl would fall for this trap
        assert ! new WackyHashCode().is(new WackyHashCode())
    }
}

class WackyHashCode {
    int hashCode(){ return 1;}
}

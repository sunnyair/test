package kotlinExample

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestCoroutine {
    fun test() {
        //这是一个线程，执行两个方法
        GlobalScope.launch{
            while(true){
                getApples()
                eatApples()
            }
        }
    }

    //获取苹果的方法
    private suspend fun getApples(){
        //切换到IO线程
        withContext(Dispatchers.IO){
            Thread.sleep(3000)
        }
    }
    //吃苹果的方法
    private fun eatApples(){
    }
}
package kotlinExample

fun main() {
    TestObject().testDemo()
}

class TestObject {
    class Driver2 //驱动

    interface DriverMountListener { //驱动加载监听器
        fun onMount(driver: Driver2) //装载驱动
        fun onUnmount(driver: Driver2) //卸载驱动
    }

    abstract class Player //播放器

    object MusicPlayer : Player(), DriverMountListener { //音乐播放器

        override fun onMount(driver: Driver2) { //装载驱动
            println("已挂载唱片")
        }

        override fun onUnmount(driver: Driver2) { //卸载驱动
            println("已卸载唱片")
        }

        val state: Int = 0 //状态

        fun play(url: String) { //演奏音乐
            println("开始演奏音乐： $url")
        }
    }

    fun testDemo() {
        val musicPlayer = MusicPlayer //音乐播放器，object不能调用构造方法
        val driver = Driver2() //驱动

        musicPlayer.onMount(driver) //装载驱动
        musicPlayer.play("千年等一回") //播放音乐
        musicPlayer.onUnmount(driver) //卸载驱动
    }
}
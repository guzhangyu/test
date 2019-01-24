
object TestScala2 {

  def main(args: Array[String]): Unit = {

    val times=5
    times match{
       case i => println(i-1)
       case i if(i%2==1) => println(i)
    }


    class Hello(_a:Int){
      def a:Int={
        println("aa")
        this._a
      }

//      def this()=this(0)

      private val b=a

      println("hhaha")
    }

    Some(5) match{
     case Some(times)=>println(6)
    }

    object Hello{
      def apply(_a: Int): Hello = new Hello(_a)

      def test(a:Int):Int=new Hello(a).b
    }

    println(Hello.test(3))

    class Hello1(a1:Int) extends Hello(a1:Int){
      override def a:Int={
        println("jhh")
        this.a1+1
      }

//      new Hello(3).b
    }

    val h=new Hello1(1)
    println(h.a)
//    println(h.b)
  }
}

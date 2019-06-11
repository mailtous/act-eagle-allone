package com.artlongs.framework.utils.tuples;

public class Tuple5<A, B, C, D, E> extends Tuple
{
   final A one;
   final B two;
   final C three;
   final D four;
   final E five;
   
   public A getOne() { return one; }
   public B getTwo() { return two; }
   public C getThree() { return three; }
   public D getFour() { return four; }
   public E getFive() { return five; }
   
   public Tuple5(A one, B two, C three, D four, E five)
   {
      this.one = one;
      this.two = two;
      this.three = three;
      this.four = four;
      this.five = five;
   }
   
   @Override
   public String toString()
   {
      return "Tuple5(" + getOne() + "," + getTwo() + "," + getThree() + "," + getFour() + "," + getFive() + ")";
   }

   
   public boolean equals(Object obj)
   {
      if ( this == obj) return true;
      
      if (! (obj instanceof Tuple5)) return false;
      
      Tuple5<?,?,?,?,?> tuple = (Tuple5<?,?,?,?,?>)obj;

      return (this.one == null ? tuple.one == null : this.one.equals(tuple.one))
            && (this.two == null ? tuple.two == null : this.two.equals(tuple.two))
            && (this.three == null ? tuple.three == null : this.three.equals(tuple.three))
            && (this.four == null ? tuple.four == null : this.four.equals(tuple.four))
            && (this.five == null ? tuple.five == null : this.five.equals(tuple.five));
   }
   
   public int hashCode()
   {
      return one.hashCode() + two.hashCode() + three.hashCode() + four.hashCode()
         + five.hashCode();
   }
   
}

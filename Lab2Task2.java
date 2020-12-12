package MeresaMeron_Lab2;

/**
 *
 * @author Meron
 */
public class Lab2Task2 {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
       //create a rational x,y,z
       Rational x,y,z;
        
       /**
        * Testing the mathematics
        */
   

        // 1/2 + 1/3 = 5/6
        x = new Rational(1, 2);
        y = new Rational(1, 3);
        z = x.add(y);
        System.out.println(z);

        // 8/9 - 1/9 = 7/9
        x = new Rational(8, 9);
        y = new Rational(1, 9);
        z = x.subtract(y);
        System.out.println(z);

        // 1/200000000 + 1/300000000 = 3/2
        x = new Rational(1, 200000000);
        y = new Rational(1, 300000000);
        z = x.divide(y);
        System.out.println(z.reducedStringValue);

        //  4/17 or 17/4 = 17/4
        x = new Rational(4, 17);
        y = new Rational(17, 4);
        z = x.maximum(y);
        System.out.println(z);

        // 3037141/3247033 * 3037547/3246599 = 841/961 
        //  If denominator is > numerator pritnt true
        x = new Rational(3037141, 3247033);
        y = new Rational(3037547, 3246599);
        z = x.multiply(y);
        System.out.println(z.isProper());

        // 1/6 or -4/-8 = 1/6
        x = new Rational( 1,  6);
        y = new Rational(-4, -8);
        z = x.minimum(y);
        System.out.println(z);

        // 0/5 = 0
        x = new Rational(0,  5);
        System.out.println(x);
        System.out.println(x.add(x).compareTo(x) == 0);
        
        // -1 = 1
        x = new Rational(-1);
        System.out.println(x.abs());
      // 6 = 6 ? true
      x = new Rational(6);
      y = new  Rational(6);
      System.out.println(x.equalsExact(y));
      
      // the inverse of 3 = -3;
       x = new Rational(3);      
      System.out.println(x.negate());
      
       // 3^5 = 243 
       x = new Rational(3);
       System.out.println(x.pow(5));
       
       // 3 = 1/3
        x = new Rational(3);
        System.out.println(x.reciprocal());
        
        // 3 = 3/1 = 1
        y = new Rational(3);
        System.out.println(y.signum());
        // 6 = string 6
       z = new Rational(6);
       System.out.println(z.toString());
       
       
       
      
      

    }

}
    


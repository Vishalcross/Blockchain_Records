import java.util.*;
import java.math.BigInteger;
import java.security.*;
class Group{
    BigInteger generator;
    BigInteger prime;
    Group(){
        this.prime = BigInteger.probablePrime(10,new Random());
        System.out.println("Prime is: "+this.prime.longValue());
        // prime = 761;
        getGenerator();
        System.out.println(generator.longValue());
        // testForGenerator();
    }
    // boolean testForGenerator(){
    //     // boolean result = true;
    //     HashMap<Integer,Boolean> map = new HashMap<>();
    //     for(int i=1;i<prime;i++){
    //         map.put(i,false);
    //     }
    //     int n = 1;
    //     while(!map.isEmpty()){
    //         if(n == prime) {
    //             // result = false;
    //             System.out.println("Nope");
    //             return false;
    //         }
    //         map.remove( FastExpModP(generator, n, prime) );
    //         n++;
    //     }
    //     System.out.println("True");
    //     return true;
    // }
    void getGenerator(){
        Factorization factorizer = new Factorization(true);
        BigInteger m = prime.subtract(BigInteger.ONE);
        ArrayList<BigInteger> factors = factorizer.getPrimeFactors(m);
        // for(int i=0;i<factors.size();i++){
        //     System.out.println(factors.get(i).longValue());
        // }
        // for (int i = 2; i <= m; i++) {
        //     int test = 0;
        //     for (int q = 0; q < factors.size(); q++) {
        //         test = FastExpModP(i, m/factors.get(q), prime);
        //         if (test==1) {
        //             System.out.println(i);
        //             break;
        //         }
        //     }
        //     System.out.println(test);
        //     if (test > 1) {
        //         this.generator = i;
        //         break;
        //     }
        // }
        // i <= m
    //     for (BigInteger bi = BigInteger.valueOf(5);
    //         bi.compareTo(BigInteger.TEN) < 1;
    //         bi = bi.add(BigInteger.ONE)) {
    //
    //     System.out.println(bi);
    // }
        for(BigInteger i = BigInteger.valueOf(2);i.compareTo(m) < 1;i = i.add(BigInteger.ONE)){
            // System.out.println(i.longValue());
            // System.out.println(m.longValue());
            // System.out.println(i.compareTo(m));

            BigInteger test = BigInteger.ZERO;
            for(int q = 0; q < factors.size();q++){
                test = i.modPow(m.divide(factors.get(q)),prime);
                // System.out.println("test is");
                // System.out.println(test);
                if(test.equals(BigInteger.ONE)){
                    break;
                }
            }
            if(test.compareTo(BigInteger.ONE) == 1){
                this.generator = i;
                // System.out.println(i);
                break;
            }
        }
    }

    static int FastExpModP(int x, int n, int p) {
        if (n == 0) {
            return 1;
        } else if (n == 1) {
            return x;
        } else if (n % 2==0) {
            return(FastExpModP( (x%p * x%p)% p, n/2, p));
        } else {
            return((x * FastExpModP( (x%p * x%p)% p, (n-1)/2, p)) % p);
        }
    }
}

class Factorization
{
    boolean printing;
    ArrayList<BigInteger>primeFactors=new ArrayList<BigInteger>();
    final BigInteger TWO=BigInteger.valueOf(2);
    BigInteger randomNum=BigInteger.ONE;
    boolean fail;
    public Factorization(boolean printing) {this.printing=printing;}
    public void factorRho(BigInteger num)
    {
        if (num.compareTo(BigInteger.ONE)==0)
            return;
        if (num.isProbablePrime(300))
        {
            primeFactors.add(num);
            return;
        }
        BigInteger divisor = rho(num);
        if(fail)
            assignRandomNum();
        factorRho(divisor);
        factorRho(num.divide(divisor));
    }
    public ArrayList<BigInteger> getPrimeFactors(BigInteger num)
    {
        primeFactors.clear();
        factorRho(num);
        // if(printing)
        //     System.out.println("The Pollard Rho algorithim has the function x^2"+((randomNum.compareTo(BigInteger.ZERO)>0)?"+":"")+randomNum+"\nThe factors are:");
        return primeFactors;
    }
  //   public static void main(String[] args)
  //   {
  //       System.out.println("This Program will return the prime factors of any number using the Pollard Rho Algorithm\n");
  //       Factorization k=new Factorization(true);
  // Scanner input=new Scanner(System.in);
  // System.out.println(k.getPrimeFactors(new BigInteger (input.next())));
  //   }
    private BigInteger rho(BigInteger num)
    {
        BigInteger x1 = TWO, x2 = TWO, divisor=BigInteger.ONE;
        if (num.mod(TWO).compareTo(BigInteger.ZERO)==0)
            return TWO;
        while(divisor.compareTo(BigInteger.ONE)==0)
        {
            x1 = f(x1).mod(num);
            x2 = f(f(x2)).mod(num);
            divisor =x1.subtract(x2).gcd(num);
        }
        if(divisor.equals(num))
        {
            // if(printing)
            //     System.out.println("The Pollard Rho algorithim has failed for the function x^2"+((randomNum.compareTo(BigInteger.ZERO)>0)?"+":"")+randomNum);
            fail=true;
        }
        return divisor;
    }
    private void assignRandomNum()
    {
        randomNum=BigInteger.valueOf((long)(100*Math.random()-50));
        fail=false;
     }
    private BigInteger f(BigInteger x)
    {
        return x.multiply(x).add(randomNum);
    }
}

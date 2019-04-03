import java.util.*;
import java.math.BigInteger;
import java.security.*;
class Group{
    int generator;
    int prime;
    Group(){
        this.prime = BigInteger.probablePrime(10,new Random()).intValue();
        // prime = 761;
        getGenerator();
        // testForGenerator();
    }
    boolean testForGenerator(){
        // boolean result = true;
        HashMap<Integer,Boolean> map = new HashMap<>();
        for(int i=1;i<prime;i++){
            map.put(i,false);
        }
        int n = 1;
        while(!map.isEmpty()){
            if(n == prime) {
                // result = false;
                System.out.println("Nope");
                return false;
            }
            map.remove( FastExpModP(generator, n, prime) );
            n++;
        }
        System.out.println("True");
        return true;
    }
    void getGenerator(){
        PollardRho factorizer = new PollardRho(true);
        ArrayList<Integer> factors = factorizer.getFactors(new BigInteger(String.valueOf(prime-1)));
        int m = prime - 1;
        for (int i = 2; i <= m; i++) {
            int test = 0;
            for (int q = 0; q < factors.size(); q++) {
                test = FastExpModP(i, m/factors.get(q), prime);
                if (test==1) {
                    System.out.println(i);
                    break;
                }
            }
            System.out.println(test);
            if (test > 1) {
                this.generator = i;
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

class PollardRho{
    boolean printing;
    ArrayList<BigInteger>primeFactors = new ArrayList<BigInteger>();
    final BigInteger TWO=BigInteger.valueOf(2);
    BigInteger randomNum=BigInteger.ONE;
    boolean fail;
    public PollardRho(boolean printing) {
        this.printing=printing;
    }
    public void factorRho(BigInteger num){
        if (num.compareTo(BigInteger.ONE)==0)
            return;
        if (num.isProbablePrime(300)){
            primeFactors.add(num);
            return;
        }
        BigInteger divisor = rho(num);
        if(fail)
            assignRandomNum();
        factorRho(divisor);
        factorRho(num.divide(divisor));
    }

    public ArrayList<Integer> getPrimeFactors(BigInteger num)
    {
        primeFactors.clear();
        factorRho(num);
        // if(printing)
        //     System.out.println("The Pollard Rho algorithim has the function x^2"+((randomNum.compareTo(BigInteger.ZERO)>0)?"+":"")+randomNum+"\nThe factors are:");
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(primeFactors.get(0).intValue());
        int j = 1;
        for(BigInteger i : primeFactors){
            System.out.println("asdasd");
            if(temp.get(j-1) != i.intValue()){
                temp.add(i.intValue());
            }
        }
        return temp;
    }

    ArrayList<Integer> getFactors(BigInteger number){
        System.out.println("This Program will return the prime factors of any number using the Pollard Rho Algorithm\n");
        PollardRho k = new PollardRho(true);
        ArrayList<Integer> temp = k.getPrimeFactors(number);
        for(Integer i: temp)
            System.out.println(i);
        return temp;
    }

    private BigInteger rho(BigInteger num){
        BigInteger x1 = TWO, x2 = TWO, divisor=BigInteger.ONE;
        if (num.mod(TWO).compareTo(BigInteger.ZERO)==0)
            return TWO;
        while(divisor.compareTo(BigInteger.ONE)==0){
            x1 = f(x1).mod(num);
            x2 = f(f(x2)).mod(num);
            divisor =x1.subtract(x2).gcd(num);
        }
        if(divisor.equals(num)){
            // if(printing)
            //     System.out.println("The Pollard Rho algorithim has failed for the function x^2"+((randomNum.compareTo(BigInteger.ZERO)>0)?"+":"")+randomNum);
            fail=true;
        }
        return divisor;
    }

    private void assignRandomNum(){
        randomNum=BigInteger.valueOf((long)(100*Math.random()-50));
        fail=false;
    }

    private BigInteger f(BigInteger x){
        return x.multiply(x).add(randomNum);
    }
}

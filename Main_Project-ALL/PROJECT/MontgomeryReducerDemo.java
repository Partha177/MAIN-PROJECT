
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.io.DataInputStream;
import java.util.Random;
 

public class MontgomeryReducerDemo {




	private BigInteger p;
    private BigInteger q;
    private BigInteger mod;
    private BigInteger phi;
    private BigInteger y;
    private BigInteger d;
    private int        bitlength = 1024;
    private Random     r;
 
    public MontgomeryReducerDemo()
    {
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r);
        q = BigInteger.probablePrime(bitlength, r);
        mod= p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        y = BigInteger.probablePrime(bitlength / 2, r);
        while (phi.gcd(y).compareTo(BigInteger.ONE) > 0 && y.compareTo(phi) < 0)
        {
            y.add(BigInteger.ONE);
        }
        d = y.modInverse(phi);
    }
 
    public MontgomeryReducerDemo(BigInteger y, BigInteger d, BigInteger mod)
    {
        this.y = y;
        this.d = d;
        this.mod = mod;
    }
 


	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		// Prompt user on standard output, parse standard input
		MontgomeryReducerDemo rsa= new MontgomeryReducerDemo();
		String input_string;
		DataInputStream in = new DataInputStream(System.in);
		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "US-ASCII"));
		System.out.print("Enter Plaintext");
		input_string = in.readLine();
		System.out.println("Encrypting String: " + input_string);
        System.out.println("String in Bytes: " + bytesToString(input_string.getBytes()));
		
		System.out.print("Operation (\"times\" or \"pow\"): ");
		String oper = in.readLine();
		
		BigInteger x = new BigInteger(input_string);
		
		System.out.println("String in Bytes: " + x);
		
		byte[] encrypted = rsa.encrypt(input_string.getBytes());
		System.out.println("Encrypted text: " + bytesToString(encrypted));

		
		rsa.function(x,oper);
	}
	
	public void function(BigInteger x,String oper) {
		MontgomeryReducer red = new MontgomeryReducer(mod);
		BigInteger xm = red.convertIn(x);
		BigInteger zm;
		BigInteger z;
		if (oper.equals("times")) {
			zm = red.multiply(xm, red.convertIn(y));
			z = x.multiply(y).mod(mod);
		} else if (oper.equals("pow")) {
			zm = red.pow(xm, y);
			z = x.modPow(y, mod);
		} else
			throw new IllegalArgumentException("Invalid operation: " + oper);
		if (!red.convertOut(zm).equals(z))
			throw new AssertionError("Self-check failed");
		System.out.printf("%d%s%d mod %d%n", x, oper.equals("times") ? " * " : "^", y, mod);
		System.out.println("= " + z);
	}

	// Encrypt message
    public byte[] encrypt(byte[] message)
    {
        return (new BigInteger(message)).modPow(y,mod).toByteArray();
    }

    private static String bytesToString(byte[] encrypted)
    {
        String test = "";
        for (byte b : encrypted)
        {
            test += Byte.toString(b);
        }
        return test;
    }
 
}
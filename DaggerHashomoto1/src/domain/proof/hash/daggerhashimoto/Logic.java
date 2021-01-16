package domain.proof.hash.daggerhashimoto;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import domain.proof.hash.SHA512.SHA512;

public class Logic {
	protected static int NUMBER_OF_BITS = 512;
	private static BigInteger moduloValue = new BigInteger("256");

//	Changed from private to protected to this test in DAGgeneration
	protected static String encode_integer(BigInteger x) {
		String o = "";
		for (int i = 0; i < (NUMBER_OF_BITS / 8); i++) {
			int modResult = (x.mod(moduloValue)).intValue();
			if (modResult != 0) {
				o = (char) modResult + o;
				x = x.divide(moduloValue);
			}
		}
		return o;
	}

//	Changed from private to protected to this test in DAGgeneration
	protected static BigInteger decode_integer(String s) {
		BigInteger x = new BigInteger("0");
		for (char c : s.toCharArray()) {
			x = x.multiply(moduloValue);
			x = x.add(BigInteger.valueOf(c));
		}
		return x;
	}

	protected static BigInteger sha512(BigInteger x) {
		String s = null;
		if (x == (BigInteger) x) {
			s = encode_integer(x);
		}
		return decode_integer(SHA512.hash(s.getBytes()));
	}

	protected static BigInteger doubleSha512(BigInteger x) {
		String s = null;
		if (x == (BigInteger) x) {
			s = encode_integer(x);
		}
		return decode_integer(SHA512.hash(SHA512.hash(s.getBytes()).getBytes()));
	}

	// Light Client Evaluation
	public static BigInteger quickCalc(BigInteger seed, int p) {
		BigInteger P = Constants.P;
		int w = Constants.w;
		BigInteger w_1 = BigInteger.valueOf(w);
		Map<Integer, BigInteger> cache = new HashMap<Integer, BigInteger>();

		class Local {
			BigInteger quickCalcCached(int p) {
				if (cache.containsKey(p)) {
					System.out.println("quickCalc test cache contains p");
				} else if (p == 0) {
					System.out.println("quickCalc test p = 0" + p);
//					cache.put(p, (int) Math.pow(sha512(seed), w) % (int) P);
					cache.put(p, sha512(seed).pow(w).mod(P));
				} else {
					System.out.println("quickCalc test p is: " + p);
//					int x = (int) Math.pow(sha512(seed), (p + 1) * w) % (int) P;
					BigInteger x = sha512(seed).pow(p + 1).multiply(w_1).mod(P);
					for (int i = 0; i < Constants.k; i++) {
//						x ^= quickCalcCached(x % p);
						x = x.xor(quickCalcCached(x.mod(BigInteger.valueOf(p)).intValue()));
					}
//					cache.put(p, (int) Math.pow(x, w) % (int) P);
					cache.put(p, x.pow(w).mod(P));
				}
				return cache.get(p);
			}
		}
		return new Local().quickCalcCached(p);
	}

}

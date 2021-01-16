package domain.proof.hash.daggerhashimoto;

import java.math.BigInteger;

public class Constants {
	// Largest Safe Prime less than 2 to the power of 512
	// protected static double SAFE_PRIME_512 = ( Math.pow(2, 512) - 38117);
	private static BigInteger substractionValue = new BigInteger("38117");
	// use BigInteger is more accurate than double (double might drop small
	// amounts)
	private static BigInteger SAFE_PRIME_512 = new BigInteger("2").pow(512).subtract(substractionValue);

	// Size of the dataset (4 Gigabytes); MUST BE MULTIPLE OF 65536
	protected static long n = ((4000055296L * 8) / Logic.NUMBER_OF_BITS);
	/*
	 * Increment in value of n per period; MUST BE MULTIPLE OF 65536 with
	 * epochtime = 20000 gives 882 MB growth per year
	 */
	protected static int n_increment = 65536;

	// Size of the light client's cache (can be chosen by light client; not part of
	// the algo spec)
	protected static int cache_size = 2500;

	// Difficulty (adjusted during block evaluation)
	protected static double diff = Math.pow(2, 14);
	// Length of an epoch in blocks (how often the dataset is updated)
	protected static int epochtime = 100000;
	// Number of parents of a node
	protected static short k = 1;
	// Used for modular exponentiation hashing
	// I am not sure what number should be here but we are using SHA512 so I guess
	// thats where we take it from
	// no calculation power for that so 8 like 8 bits?
	protected static int w = 16;
	// Number of dataset accesses during hashimoto
	protected static int access = 200;
	// Safe Prime for hashing and random number generation
	public static BigInteger P = SAFE_PRIME_512;

}

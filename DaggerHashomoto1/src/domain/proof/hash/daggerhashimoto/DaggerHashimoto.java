package domain.proof.hash.daggerhashimoto;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//import domain.Asset;
//import domain.Block;
//import domain.Chain;
//import domain.Transaction;

public class DaggerHashimoto {
	
//	public static BigInteger origHashimoto(Block block, long nonce) {
//		BigInteger previousHash = block.getHashPrevious();
//		BigInteger merkleRoot = block.getMerkleRoot();
//		List<Transaction> listOfTransactions = block.getTransactions();
////		int hashOutputA = Logic.sha512(previousHash.intValue() + merkleRoot.intValue() + (int) nonce);
//		BigInteger hashOutputA = Logic.sha512(previousHash.add(merkleRoot).add(BigInteger.valueOf(nonce)));
////		int txidMix = 0;
//		BigInteger txidMix = BigInteger.valueOf(0);
//		for (int i = 0; i < 64; i++) {
////			int shiftedA = hashOutputA >> i;
//			BigInteger shiftedA = hashOutputA.shiftRight(i);
////			int transaction = shiftedA % listOfTransactions.size();
//			BigInteger transaction = shiftedA.mod(BigInteger.valueOf(listOfTransactions.size()));
////			txidMix ^= listOfTransactions.get(transaction).getOwnHash().intValue() << i;
////			Either shift left after applying xor or before, shouldn't make a difference...
////			txidMix = txidMix.xor(listOfTransactions.get(transaction.intValue()).getOwnHash()).shiftLeft(i);
//			txidMix = txidMix.xor(listOfTransactions.get(transaction.intValue()).getOwnHash().shiftLeft(i));
//		}
////		return txidMix ^ ((int)nonce << 192);
//		return txidMix.xor(BigInteger.valueOf(nonce).shiftLeft(192));
//	}
	
	public static BigInteger hashimoto(LinkedList<BigInteger> dag, int dagSize, BigInteger header, long nonce) {
//		m = dagSize / 2
		int m = dagSize / 2;
//		mix = Logic.sha512(encode_integer(BigInteger.valueOf(nonce) + header)
//		but the encode_integer() returns a String. The sha512() already does encode_integer inside...
		BigInteger mix = Logic.sha512(BigInteger.valueOf(nonce).add(header));

		for (int i = 0; i < Constants.access; i++) {
			BigInteger value_1 = BigInteger.valueOf(2).pow(64); // 2**64
			BigInteger value_2 = mix.mod(value_1); // mix % 2**64
//			mix ^= dag.get(m + (mix % 2**64) % m)
			mix = mix.xor(dag.get(BigInteger.valueOf(m).add(value_2).mod(BigInteger.valueOf(m)).intValue()));
		}
		return Logic.doubleSha512(mix);
	}
	
	public static BigInteger quickHashimoto(BigInteger seed, int dagSize, BigInteger header, long nonce) {
//		m = dagSize // 2
		int m = Math.floorDiv(dagSize, 2);
		BigInteger mix = Logic.sha512(BigInteger.valueOf(nonce).add(header));

		for (int i = 0; i < Constants.access; i++) {
//			we can make this formula work with all BigInteger by making Loig.quickCalc() accept a BigInteger as well instead of int.
//			Hence, we wont need to use the intValue of the BigInteger at the end
			BigInteger value_1 = BigInteger.valueOf(2).pow(64); // 2**64
			BigInteger value_2 = mix.mod(value_1); // mix % 2**64
//			mix ^= Logic.quick_calc(seed, m + (mix % 2**64) % m)
			mix = mix.xor(
					Logic.quickCalc(seed, BigInteger.valueOf(m).add(value_2).mod(BigInteger.valueOf(m)).intValue()));
		}
		return Logic.doubleSha512(mix);
	}
	
}

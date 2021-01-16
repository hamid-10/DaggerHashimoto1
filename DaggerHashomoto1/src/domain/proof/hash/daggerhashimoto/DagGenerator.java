package domain.proof.hash.daggerhashimoto;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

//import domain.Asset;
//import domain.Block;
//import domain.Chain;

public class DagGenerator {

	public DagGenerator() {
		super();
	}

	public LinkedList<BigInteger> createDaggGraph(BigInteger seed, int length) {
		BigInteger P = Constants.P;
		BigInteger init, picker;
		// picker = init = (int) ((Math.pow(sha512(seed), Constants.w)) % P) ;
//      changed this to work with sha512(seed).pow... instead of seed.pow...
		picker = init = Logic.sha512(seed).pow(Constants.w).mod(P);
		LinkedList<BigInteger> o = new LinkedList<BigInteger>() {
			{
				add(init);
			}
		};
		for (int i = 1; i < length; i++) {
			BigInteger x = picker = picker.multiply(init).mod(P);
			for (int j = 0; j < Constants.k; j++) {
				// x ^= o[(x % i)]
//				x = x.modPow(o.getLast(), P);
//				Changed this to work with XOR here:
				x = x.xor(o.get(x.mod(BigInteger.valueOf(i)).intValue()));
			}
			o.addLast(x.pow(Constants.w).mod(P));
		}
		return o;
	}

//	public BigInteger getPreviousHash(int n) throws NoSuchAlgorithmException, IOException {
//		Chain chain = Chain.instance(Asset.getDefault());
//		BigInteger genesisPrevHash = chain.getGenesisBlock().getHashPrevious();
////		Assuming num is chain length for now...
//		int num = chain.size();
//		if (num <= 0) {
////			the implementation of hash_to_int goes here: TBD ...
//			return genesisPrevHash;
//		} else {
////			Think about the previous hash and the use of (n - 1), might need only n
////			as the getHashPrevious gives already the previous hash...
//			BigInteger previousHash = chain.getBlock(n - 1).getHashPrevious();
////			for now using this...Radix 16
////			return Logic.decode_integer(previousHash.toString(16));
//			return previousHash;
//		}
//	}

//	public Map<String, BigInteger> getSeedset(Block block) throws NoSuchAlgorithmException, IOException {
//		Map<String, BigInteger> seedset = new HashMap<String, BigInteger>();
//		Chain chain = Chain.instance(Asset.getDefault());
////		created a method getBlockNumber() in Chain class to get at which index this block is stored
////		it is not clear what is meant by block.number: 
////		Number of blocks (chain.getNumberOfBlocks()) OR number of the block (index) in the chain (chain.getBlockNumber(block))
////		We use the number of blocks (chain.getNumberOfBlocks()) for now... if it is meant to be the other option we do the following
////		- uncomment nex line and - comment out the line after
////		BigInteger blockNumber = chain.getBlockNumber(block);
//		BigInteger blockNumber = chain.getNumberOfBlocks();
////		seedset.put("back_number", BigInteger.valueOf(blockNumber - (blockNumber % Constants.epochtime)));
//		seedset.put("back_number", blockNumber.subtract(blockNumber.mod(BigInteger.valueOf(Constants.epochtime))));
//		seedset.put("back_hash", getPreviousHash(seedset.get("back_number").intValue()));
//		seedset.put("front_number",
//				seedset.get("back_number").subtract(BigInteger.valueOf(Constants.epochtime)).max(new BigInteger("0")));
//		seedset.put("front_hash", getPreviousHash(seedset.get("front_number").intValue()));
//		return seedset;
//	}

//	public int getDagSize(Block block) throws NoSuchAlgorithmException, IOException {
//		Chain chain = Chain.instance(Asset.getDefault());
////		We use the number of blocks (chain.getNumberOfBlocks()) for now... if it is meant to be the other option we do the following
////		- uncomment nex line and - comment out the line after
////		BigInteger blockNumber = chain.getBlockNumber(block);
//		BigInteger blockNumber = chain.getNumberOfBlocks();
//		return (int) Constants.n + (Math.floorDiv(blockNumber.intValue(), Constants.epochtime) * Constants.n_increment);
//	}

//	public HashMap<String, HashMap<String, Object>> getDaggerSet(Block block)
//			throws NoSuchAlgorithmException, IOException {
//		int dagsize = getDagSize(block);
//		Map<String, BigInteger> seedset = getSeedset(block);
////		Used HashMap<String, Object> instead of HashMap<String, BigInteger> because this map will contain dags(LinkedList) and BigIntegers
////		So make sure to specify (cast) the object when retrieving it from the hashMap
//		HashMap<String, HashMap<String, Object>> maps = new HashMap<String, HashMap<String, Object>>();
//		HashMap<String, Object> detailsFront = new HashMap<String, Object>();
//		HashMap<String, Object> detailsBack = new HashMap<String, Object>();
//		BigInteger zero = new BigInteger("0");
//		int resultOfComparison = (seedset.get("front_hash")).compareTo(zero);
//		if (resultOfComparison <= 0) {
//			// no back buffer is possible, only front buffer
//			detailsFront.put("dag", createDaggGraph(seedset.get("front_hash"), dagsize));
//			detailsFront.put("block_number", zero);
//			maps.put("front", detailsFront);
//			return maps;
//		} else {
//			detailsFront.put("dag", createDaggGraph(seedset.get("front_hash"), dagsize));
//			detailsFront.put("block_number", seedset.get("front_number"));
//			maps.put("front", detailsFront);
//
//			detailsBack.put("dag", createDaggGraph(seedset.get("back_hash"), dagsize));
//			detailsBack.put("block_number", seedset.get("back_number"));
//			maps.put("back", detailsBack);
//			return maps;
//		}
//	}

	public static void main(String[] args) {

		System.out.println("decoder " + Logic.decode_integer("Hello there"));

		BigInteger big = new BigInteger("87521618088882658227876453");
		System.out.println("encoder " + Logic.encode_integer(big));

		System.out.println("SHA3: " + Logic.sha512(big));
		System.out.println("Double SHA3: " + Logic.doubleSha512(big));

		DagGenerator dagGenerator = new DagGenerator();
		System.out.println("Graph " + dagGenerator.createDaggGraph(big, 10));

		System.out.println();
		System.out.println(Constants.P);
		System.out.println((Constants.P).longValue());
		System.out.println((Constants.P).doubleValue());

//      Testing quickCalc() which is in Logic...  
		System.out.println("quickCalc() here: " + Logic.quickCalc(big, 2));

//      Some calculation tests:
		System.out.println("expo: " + Math.pow(2, 64));
		System.out.println(BigInteger.valueOf(2).pow(64));
		System.out.println(BigInteger.valueOf(Double.valueOf(Math.pow(2, 64)).longValue()));

		System.out.println(Double.valueOf(Math.pow(10, 19)).longValue());
		System.out.println(Long.MAX_VALUE);

		System.out.println(Double.MAX_VALUE);

		System.out.println(Long.MAX_VALUE);

		System.out.println(new BigInteger("7").add(new BigInteger("875216180888826582278764536489264895208284347530920")
				.mod(BigInteger.valueOf(2).pow(64)).mod(new BigInteger("7"))).intValue());

		System.out.println("quickHashimoto Here: "
				+ DaggerHashimoto.quickHashimoto(new BigInteger("875216180888826582278764536489264895208284347530920"),
						678, new BigInteger("8752161808888265822787645364892648952"), 273495));
	}

}

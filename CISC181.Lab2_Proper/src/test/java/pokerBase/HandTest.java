package pokerBase;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.DeckException;
import exceptions.HandException;
import pokerEnums.eCardNo;
import pokerEnums.eHandStrength;
import pokerEnums.eRank;
import pokerEnums.eSuit;

public class HandTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private Hand SetHand(ArrayList<Card> setCards, Hand h)
	{
		Object t = null;
		
		try {
			//	Load the Class into 'c'
			Class<?> c = Class.forName("pokerBase.Hand");
			//	Create a new instance 't' from the no-arg Deck constructor
			t = c.newInstance();
			//	Load 'msetCardsInHand' with the 'Draw' method (no args);
			Method msetCardsInHand = c.getDeclaredMethod("setCardsInHand", new Class[]{ArrayList.class});
			//	Change the visibility of 'setCardsInHand' to true *Good Grief!*
			msetCardsInHand.setAccessible(true);
			//	invoke 'msetCardsInHand'
			Object oDraw = msetCardsInHand.invoke(t, setCards);
			
		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		h = (Hand)t;
		return h;
		
	}
	/**
	 * This test checks to see if a HandException is throw if the hand only has two cards.
	 * @throws Exception
	 */
	@Test(expected = HandException.class)
	public void TestEvalShortHand() throws Exception {
		
		Hand h = new Hand();
		
		ArrayList<Card> ShortHand = new ArrayList<Card>();
		ShortHand.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		ShortHand.add(new Card(eSuit.CLUBS,eRank.ACE,0));

		h = SetHand(ShortHand,h);
		
		//	This statement should throw a HandException
		h = Hand.EvaluateHand(h);	
	}	
			
	@Test
	public void TestFourOfAKind1() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> FourOfAKind = new ArrayList<Card>();
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));		
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.KING,0));
		
		Hand h = new Hand();
		h = SetHand(FourOfAKind,h);
		
		boolean bActualIsHandFourOfAKind = Hand.isHandFourOfAKind(h, hs);
		boolean bExpectedIsHandFourOfAKind = true;
		
		//	Did this evaluate to Four of a Kind?
		assertEquals(bActualIsHandFourOfAKind,bExpectedIsHandFourOfAKind);		
		//	Was the four of a kind an Ace?
		assertEquals(hs.getHiHand(),eRank.ACE.getiRankNbr());		
		//	FOAK has one kicker.  Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		//	FOAK has one kicker.  Was it a King?		
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
	}
	
	@Test
	public void TestFourOfAKind2() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> FourOfAKind = new ArrayList<Card>();
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.KING,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.KING,0));		
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.KING,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.KING,0));
		
		Hand h = new Hand();
		h = SetHand(FourOfAKind,h);
		
		boolean bActualIsHandFourOfAKind = Hand.isHandFourOfAKind(h, hs);
		boolean bExpectedIsHandFourOfAKind = true;
		
		//	Did this evaluate to Four of a Kind?
		assertEquals(bActualIsHandFourOfAKind,bExpectedIsHandFourOfAKind);		
		//	Was the four of a kind a King?
		assertEquals(hs.getHiHand(),eRank.KING.getiRankNbr());		
		//	FOAK has one kicker.  Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		//	FOAK has one kicker.  Was it an ACE?		
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
	}
	
	@Test
	public void TestRoyalFlush() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> RoyalFlush = new ArrayList<Card>();

		RoyalFlush.add(new Card(eSuit.CLUBS,eRank.ACE,0));
	
		RoyalFlush.add(new Card(eSuit.CLUBS,eRank.KING,0));

		RoyalFlush.add(new Card(eSuit.CLUBS,eRank.QUEEN,0));	

		RoyalFlush.add(new Card(eSuit.CLUBS,eRank.JACK,0));
		RoyalFlush.add(new Card(eSuit.CLUBS,eRank.TEN,0));
		Hand h = new Hand();
		h = SetHand(RoyalFlush,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestRoyalFlush failed");
		}
		
		boolean bActualIsHandRoyalFlush= Hand.isHandRoyalFlush(h, hs);
		boolean bExpectedIsHandRoyalFlush = true;
		
		//	Did this evaluate to Royal Flush?
		assertEquals(bActualIsHandRoyalFlush,bExpectedIsHandRoyalFlush);		
		//	Was the high hand an Ace?
		assertEquals(hs.getHiHand(),eRank.ACE.getiRankNbr());		
		//	RF has no kickers
	}
	
	
	@Test
	public void TestHandStraight1() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> HandStraight = new ArrayList<Card>();
		HandStraight.add(new Card(eSuit.CLUBS,eRank.THREE,0));
		HandStraight.add(new Card(eSuit.HEARTS,eRank.FOUR,0));
		HandStraight.add(new Card(eSuit.CLUBS,eRank.FIVE,0));		
		HandStraight.add(new Card(eSuit.CLUBS,eRank.SIX,0));
		HandStraight.add(new Card(eSuit.CLUBS,eRank.SEVEN,0));
		
		Hand h = new Hand();
		h = SetHand(HandStraight,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestHandStraight failed");
		}
		
		boolean bActualIsHandStraight = Hand.isHandStraight(h, hs);
		boolean bExpectedIsHandStraight = true;
		
		//	Did this evaluate to a Straight?
		assertEquals(bActualIsHandStraight,bExpectedIsHandStraight);		
		//	Was the high hand a seven?
		assertEquals(hs.getHiHand(),eRank.SEVEN.getiRankNbr());		
		//  No Kickers
		
	}
	
	@Test
	public void TestHandStraight2() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> HandStraight = new ArrayList<Card>();
		HandStraight.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		HandStraight.add(new Card(eSuit.HEARTS,eRank.TWO,0));
		HandStraight.add(new Card(eSuit.CLUBS,eRank.THREE,0));		
		HandStraight.add(new Card(eSuit.CLUBS,eRank.FOUR,0));
		HandStraight.add(new Card(eSuit.CLUBS,eRank.FIVE,0));
		
		Hand h = new Hand();
		h = SetHand(HandStraight,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestHandStraight failed");
		}
		
		boolean bActualIsHandStraight = Hand.isHandStraight(h, hs);
		boolean bExpectedIsHandStraight = true;
		
		//	Did this evaluate to a Straight?
		assertEquals(bActualIsHandStraight,bExpectedIsHandStraight);		
		//	Was the high hand a five?
		assertEquals(hs.getHiHand(),eRank.FIVE.getiRankNbr());		
		//  No Kickers
		
	}
	
	@Test
	public void TestStraightFlush() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> StraightFlush = new ArrayList<Card>();
		StraightFlush.add(new Card(eSuit.CLUBS,eRank.THREE,0));
		StraightFlush.add(new Card(eSuit.CLUBS,eRank.FOUR,0));
		StraightFlush.add(new Card(eSuit.CLUBS,eRank.FIVE,0));		
		StraightFlush.add(new Card(eSuit.CLUBS,eRank.SIX,0));
		StraightFlush.add(new Card(eSuit.CLUBS,eRank.SEVEN,0));
		
		Hand h = new Hand();
		h = SetHand(StraightFlush,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestStraightFlush failed");
		}
		
		boolean bActualIsStraightFlush = Hand.isHandStraightFlush(h, hs);
		boolean bExpectedIsStraightFlush = true;
		
		//	Did this evaluate to a Straight Flush?
		assertEquals(bActualIsStraightFlush, bExpectedIsStraightFlush);		
		//	Was the high hand a seven?
		assertEquals(hs.getHiHand(),eRank.SEVEN.getiRankNbr());		
		//  No Kickers
		
	}
	
	@Test
	public void TestFullHouse1() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> FullHouse = new ArrayList<Card>();
		FullHouse.add(new Card(eSuit.CLUBS,eRank.THREE,0));
		FullHouse.add(new Card(eSuit.SPADES,eRank.THREE,0));
		FullHouse.add(new Card(eSuit.HEARTS,eRank.THREE,0));		
		FullHouse.add(new Card(eSuit.DIAMONDS,eRank.TWO,0));
		FullHouse.add(new Card(eSuit.CLUBS,eRank.TWO,0));
		
		Hand h = new Hand();
		h = SetHand(FullHouse,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestFullHouse failed");
		}
		
		boolean bActualIsFullHouse = Hand.isHandFullHouse(h, hs);
		boolean bExpectedIsFullHouse = true;
		
		//	Did this evaluate to a Full House?
		assertEquals(bActualIsFullHouse, bExpectedIsFullHouse);		
		//	Was the high hand a three?
		assertEquals(hs.getHiHand(),eRank.THREE.getiRankNbr());		
		//  Was the low hand a two?
		assertEquals(hs.getLoHand(),eRank.TWO.getiRankNbr());		
		//  No Kickers
	}
	
	@Test
	public void TestFullHouse2() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> FullHouse = new ArrayList<Card>();
		FullHouse.add(new Card(eSuit.CLUBS,eRank.TWO,0));
		FullHouse.add(new Card(eSuit.SPADES,eRank.TWO,0));
		FullHouse.add(new Card(eSuit.HEARTS,eRank.TWO,0));		
		FullHouse.add(new Card(eSuit.DIAMONDS,eRank.THREE,0));
		FullHouse.add(new Card(eSuit.CLUBS,eRank.THREE,0));
		
		Hand h = new Hand();
		h = SetHand(FullHouse,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestFullHouse failed");
		}
		
		boolean bActualIsFullHouse = Hand.isHandFullHouse(h, hs);
		boolean bExpectedIsFullHouse = true;
		
		//	Did this evaluate to a Full House?
		assertEquals(bActualIsFullHouse, bExpectedIsFullHouse);		
		//	Was the high hand a two?
		assertEquals(hs.getHiHand(),eRank.TWO.getiRankNbr());		
		//  Was the low hand a three?
		assertEquals(hs.getLoHand(),eRank.THREE.getiRankNbr());		
		//  No Kickers
	}
	
	@Test
	public void TestHandFlush() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> HandFlush = new ArrayList<Card>();
		HandFlush.add(new Card(eSuit.DIAMONDS,eRank.THREE,0));
		HandFlush.add(new Card(eSuit.DIAMONDS,eRank.FIVE,0));
		HandFlush.add(new Card(eSuit.DIAMONDS,eRank.SIX,0));		
		HandFlush.add(new Card(eSuit.DIAMONDS,eRank.TWO,0));
		HandFlush.add(new Card(eSuit.DIAMONDS,eRank.ACE,0));
		
		Hand h = new Hand();
		h = SetHand(HandFlush,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestHandFlush failed");
		}
		
		boolean bActualIsHandFlush = Hand.isHandFlush(h, hs);
		boolean bExpectedIsHandFlush = true;
		
		//	Did this evaluate to a flush?
		assertEquals(bActualIsHandFlush, bExpectedIsHandFlush);		
		//	Was the high hand an Ace?
		assertEquals(hs.getHiHand(),eRank.ACE.getiRankNbr());		
		//  No Kickers
	}
	
	@Test
	public void TestThreeOfAKind1() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> ThreeOfAKind = new ArrayList<Card>();
		ThreeOfAKind.add(new Card(eSuit.CLUBS,eRank.THREE,0));
		ThreeOfAKind.add(new Card(eSuit.SPADES,eRank.THREE,0));
		ThreeOfAKind.add(new Card(eSuit.HEARTS,eRank.THREE,0));		
		ThreeOfAKind.add(new Card(eSuit.DIAMONDS,eRank.TWO,0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		
		Hand h = new Hand();
		h = SetHand(ThreeOfAKind,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestThreeOfAKind failed");
		}
		
		boolean bActualIsThreeOfAKind = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsThreeOfAKind = true;
		
		//	Did this evaluate to a Three of a Kind?
		assertEquals(bActualIsThreeOfAKind, bExpectedIsThreeOfAKind);		
		//	Was the high hand a three?
		assertEquals(hs.getHiHand(),eRank.THREE.getiRankNbr());				
		//  Was there two Kickers, Two and Ace?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.TWO);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.DIAMONDS);
	}
	
	@Test
	public void TestThreeOfAKind2() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> ThreeOfAKind = new ArrayList<Card>();
		ThreeOfAKind.add(new Card(eSuit.CLUBS,eRank.TWO,0));
		ThreeOfAKind.add(new Card(eSuit.SPADES,eRank.TWO,0));
		ThreeOfAKind.add(new Card(eSuit.HEARTS,eRank.TWO,0));		
		ThreeOfAKind.add(new Card(eSuit.DIAMONDS,eRank.THREE,0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		
		Hand h = new Hand();
		h = SetHand(ThreeOfAKind,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestThreeOfAKind failed");
		}
		
		boolean bActualIsThreeOfAKind = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsThreeOfAKind = true;
		
		//	Did this evaluate to a Three of a Kind?
		assertEquals(bActualIsThreeOfAKind, bExpectedIsThreeOfAKind);		
		//	Was the high hand a two?
		assertEquals(hs.getHiHand(),eRank.TWO.getiRankNbr());				
		//  Was there two Kickers, three and Ace?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.THREE);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.DIAMONDS);
	}
	
	@Test
	public void TestThreeOfAKind3() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> ThreeOfAKind = new ArrayList<Card>();
		ThreeOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		ThreeOfAKind.add(new Card(eSuit.SPADES,eRank.ACE,0));
		ThreeOfAKind.add(new Card(eSuit.HEARTS,eRank.ACE,0));		
		ThreeOfAKind.add(new Card(eSuit.DIAMONDS,eRank.TWO,0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS,eRank.THREE,0));
		
		Hand h = new Hand();
		h = SetHand(ThreeOfAKind,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestThreeOfAKind failed");
		}
		
		boolean bActualIsThreeOfAKind = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsThreeOfAKind = true;
		
		//	Did this evaluate to a Three of a Kind?
		assertEquals(bActualIsThreeOfAKind, bExpectedIsThreeOfAKind);		
		//	Was the high hand an ace?
		assertEquals(hs.getHiHand(),eRank.ACE.getiRankNbr());				
		//  Was there two Kickers, Two and Three?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.THREE);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.TWO);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.DIAMONDS);
	}
	
	@Test
	public void TestTwoPair1() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> TwoPair = new ArrayList<Card>();
		TwoPair.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		TwoPair.add(new Card(eSuit.DIAMONDS,eRank.ACE,0));
		TwoPair.add(new Card(eSuit.SPADES,eRank.TWO,0));		
		TwoPair.add(new Card(eSuit.HEARTS,eRank.TWO,0));
		TwoPair.add(new Card(eSuit.CLUBS,eRank.KING,0));
		
		Hand h = new Hand();
		h = SetHand(TwoPair,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestTwoPair failed");
		}
		
		boolean bActualIsTwoPair = Hand.isHandTwoPair(h, hs);
		boolean bExpectedIsTwoPair = true;
		
		//	Did this evaluate to two pair?
		assertEquals(bActualIsTwoPair,bExpectedIsTwoPair);		
		//	Was the high hand an Ace?
		assertEquals(hs.getHiHand(),eRank.ACE.getiRankNbr());
		//	Was the low hand a Two?
		assertEquals(hs.getLoHand(),eRank.TWO.getiRankNbr());
		//	Two pair has 1 kicker, was it a King of Clubs?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);	
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
	}
	
	@Test
	public void TestTwoPair2() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> TwoPair = new ArrayList<Card>();
		TwoPair.add(new Card(eSuit.CLUBS,eRank.THREE,0));
		TwoPair.add(new Card(eSuit.DIAMONDS,eRank.THREE,0));
		TwoPair.add(new Card(eSuit.SPADES,eRank.TWO,0));		
		TwoPair.add(new Card(eSuit.HEARTS,eRank.TWO,0));
		TwoPair.add(new Card(eSuit.CLUBS,eRank.KING,0));
		
		Hand h = new Hand();
		h = SetHand(TwoPair,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestTwoPair failed");
		}
		
		boolean bActualIsTwoPair = Hand.isHandTwoPair(h, hs);
		boolean bExpectedIsTwoPair = true;
		
		//	Did this evaluate to a two pair?
		assertEquals(bActualIsTwoPair,bExpectedIsTwoPair);		
		//	Was the high hand a three?
		assertEquals(hs.getHiHand(),eRank.THREE.getiRankNbr());
		//	Was the low hand a Two?
		assertEquals(hs.getLoHand(),eRank.TWO.getiRankNbr());
		//	Two pair has 1 kicker, was it a King of Clubs?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);	
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
	}
	
	@Test
	public void TestTwoPair3() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> TwoPair = new ArrayList<Card>();
		TwoPair.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		TwoPair.add(new Card(eSuit.DIAMONDS,eRank.ACE,0));
		TwoPair.add(new Card(eSuit.SPADES,eRank.KING,0));		
		TwoPair.add(new Card(eSuit.HEARTS,eRank.KING,0));
		TwoPair.add(new Card(eSuit.CLUBS,eRank.QUEEN,0));
		
		Hand h = new Hand();
		h = SetHand(TwoPair,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestTwoPair failed");
		}
		
		boolean bActualIsTwoPair = Hand.isHandTwoPair(h, hs);
		boolean bExpectedIsTwoPair = true;
		
		//	Did this evaluate to a two pair?
		assertEquals(bActualIsTwoPair,bExpectedIsTwoPair);		
		//	Was the high hand an Ace?
		assertEquals(hs.getHiHand(),eRank.ACE.getiRankNbr());
		//	Was the low hand a king?
		assertEquals(hs.getLoHand(),eRank.KING.getiRankNbr());
		//	Two pair has 1 kicker, was it a queen of Clubs?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);	
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.QUEEN);
	}
	
//  Previously from the Professor's code
//	public void TestFourOfAKindEval() {
//		
//		ArrayList<Card> FourOfAKind = new ArrayList<Card>();
//		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
//		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
//		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
//		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
//		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.KING,0));
//		
//		Hand h = new Hand();
//		h = SetHand(FourOfAKind,h);
//		
//		try {
//			h = Hand.EvaluateHand(h);
//		} catch (HandException e) {			
//			e.printStackTrace();
//			fail("TestFourOfAKindEval failed");
//		}
//		HandScore hs = h.getHandScore();
//		boolean bActualIsHandFourOfAKind = Hand.isHandFourOfAKind(h, hs);
//		boolean bExpectedIsHandFourOfAKind = true;
//		
//		//	Did this evaluate to Four of a Kind?
//		assertEquals(bActualIsHandFourOfAKind,bExpectedIsHandFourOfAKind);		
//		//	Was the four of a kind an Ace?
//		assertEquals(hs.getHiHand(),eRank.ACE.getiRankNbr());		
//		//	FOAK has one kicker.  Was it a Club?
//		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
//		//	FOAK has one kicker.  Was it a King?		
//		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
//	}	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

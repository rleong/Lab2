package pokerBase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

import exceptions.DeckException;
import exceptions.HandException;
import pokerEnums.*;

import static java.lang.System.out;
import static java.lang.System.err;

public class Hand {

	private ArrayList<Card> CardsInHand;
	private ArrayList<Card> BestCardsInHand;
	private HandScore HandScore;
	private boolean bScored = false;

	public Hand() {
		CardsInHand = new ArrayList<Card>();
		BestCardsInHand = new ArrayList<Card>();
	}

	public ArrayList<Card> getCardsInHand() {
		return CardsInHand;
	}

	private void setCardsInHand(ArrayList<Card> cardsInHand) {
		CardsInHand = cardsInHand;
	}

	public ArrayList<Card> getBestCardsInHand() {
		return BestCardsInHand;
	}

	public void setBestCardsInHand(ArrayList<Card> bestCardsInHand) {
		BestCardsInHand = bestCardsInHand;
	}

	public HandScore getHandScore() {
		return HandScore;
	}

	public void setHandScore(HandScore handScore) {
		HandScore = handScore;
	}

	public boolean isbScored() {
		return bScored;
	}

	public void setbScored(boolean bScored) {
		this.bScored = bScored;
	}

	public Hand AddCardToHand(Card c) {
		CardsInHand.add(c);
		return this;
	}

	public Hand Draw(Deck d) throws DeckException {
		CardsInHand.add(d.Draw());
		return this;
	}

	/**
	 * EvaluateHand is a static method that will score a given Hand of cards
	 * 
	 * @param h
	 * @return
	 * @throws HandException
	 */
	public static Hand EvaluateHand(Hand h) throws HandException {

		Collections.sort(h.getCardsInHand());

		// Collections.sort(h.getCardsInHand(), Card.CardRank);

		if (h.getCardsInHand().size() != 5) {
			throw new HandException(h);
		}

		HandScore hs = new HandScore();
		try {
			Class<?> c = Class.forName("pokerBase.Hand");

			for (eHandStrength hstr : eHandStrength.values()) {
				Class[] cArg = new Class[2];
				cArg[0] = pokerBase.Hand.class;
				cArg[1] = pokerBase.HandScore.class;

				Method meth = c.getMethod(hstr.getEvalMethod(), cArg);
				Object o = meth.invoke(null, new Object[] { h, hs });

				// If o = true, that means the hand evaluated- skip the rest of
				// the evaluations
				if ((Boolean) o) {
					break;
				}
			}

			h.bScored = true;
			h.HandScore = hs;

		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		} catch (IllegalAccessException x) {
			x.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return h;
	}

	public static boolean isHandFiveOfAKind(Hand h, HandScore hs) {
		// Impossible!!
		hs.setHandStrength(eHandStrength.FiveOfAKind.getHandStrength());

		return false;
	}

	public static boolean isHandRoyalFlush(Hand h, HandScore hs) {
		boolean bHandCheck = false;
		// Checks whether or not the hand is a straight, and also a flush,
		// and also checks if the hand is a royal flush by looking at the
		// fourth card and seeing if it's a king or not.
		if (h.isHandStraight(h, hs) && h.isHandFlush(h, hs)
				&& h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == eRank.KING) {
			hs.setHandStrength(eHandStrength.RoyalFlush.getHandStrength());
			// Will we compare the suit value rather than the rank number???????
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			bHandCheck = true;
			//No Kickers
		}
		return bHandCheck;
	}

	public static boolean isHandStraightFlush(Hand h, HandScore hs) {
		boolean bHandCheck = false;
		// Checks whether or not the hand is a straight, and also a flush,
		// and also checks if the hand is not a royal flush by looking at the
		// fourth card and seeing if it's a king or not.
		if (h.isHandStraight(h, hs) && h.isHandFlush(h, hs)
				&& h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() != eRank.KING) {
			hs.setHandStrength(eHandStrength.StraightFlush.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			bHandCheck = true;
			//No Kickers
		}

		return bHandCheck;
	}

	public static boolean isHandFourOfAKind(Hand h, HandScore hs) {

		boolean bHandCheck = false;

		if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FourthCard.getCardNo()).geteRank()) {
			bHandCheck = true;
			hs.setHandStrength(eHandStrength.FourOfAKind.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
			hs.setKickers(kickers);

		} else if (h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
			bHandCheck = true;
			hs.setHandStrength(eHandStrength.FourOfAKind.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
			hs.setKickers(kickers);
		}
		return bHandCheck;
	}

	public static boolean isHandFullHouse(Hand h, HandScore hs) {

		boolean bHandCheck = false;
		
		//Checks for the first 3 cards and remaining 2 cards
		if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.ThirdCard.getCardNo()).geteRank()
				&& h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
			bHandCheck = true;
			hs.setHandStrength(eHandStrength.FullHouse.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr());
		//Checks for the last 3 cards and the remaining 2 cards
		} else if (h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
				.get(eCardNo.FifthCard.getCardNo()).geteRank()
				&& h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
						.get(eCardNo.SecondCard.getCardNo()).geteRank()) {
			bHandCheck = true;
			hs.setHandStrength(eHandStrength.FullHouse.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
		}
		return bHandCheck;
	}

	public static boolean isHandFlush(Hand h, HandScore hs) {
		boolean bHandCheck = false;
		
		//Checks if the suit of the first card matches the suit of the remaining
		//Cards.
		for (int i = 1; i < 5; i++) {
			if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteSuit() != h.getCardsInHand().get(i)
					.geteSuit()) {
				bHandCheck = false;
				break;
			} else {
				bHandCheck = true;
			}
		}
		if (bHandCheck && !h.isHandStraight(h, hs)) {
			hs.setHandStrength(eHandStrength.Flush.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
		}
		return bHandCheck;
	}

	public static boolean isHandStraight(Hand h, HandScore hs) {

		boolean bHandCheck = false;
		int check = 0;
		
		//Goes through all the indexes and makes sure that each card is
		//1 below the card prior in terms of rank
		for (int i = 0; i < 4; i++) {
			if (h.getCardsInHand().get(i).geteRank().getiRankNbr()
					- h.getCardsInHand().get(i + 1).geteRank().getiRankNbr() == 1) {
				check +=1;
			}
		}
		//Testing (not part of the code)
		System.out.println(check);
		for (int i = 0; i < 5; i++) {
			System.out.println(h.getCardsInHand().get(i).geteRank());
		}
		// Ace Checking, checks if there is already a straight excluding the Ace,
		// Then checks if the last card is a two, then this will recognize that
		// this hand is indeed a straight, and sets the highest value to Four rather
		// than Ace.
		if (check == 3 && h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == eRank.ACE
				&& h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank() == eRank.TWO) {
			check = 4;
			bHandCheck = true;
			hs.setHandStrength(eHandStrength.Straight.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
		//Otherwise, it is a regular straight
		} else if (check == 4) {
			bHandCheck = true;
			hs.setHandStrength(eHandStrength.Straight.getHandStrength());
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(0);
		}
		return bHandCheck;
	}

	public static boolean isHandThreeOfAKind(Hand h, HandScore hs) {
		boolean bHandCheck = false;
		//Makes sure that the hand is not accidentally a Full house
		if(!h.isHandFullHouse(h, hs)){
			// First three are the same, while the last two are not
			if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.ThirdCard.getCardNo()).geteRank()) {
				bHandCheck = true;
				hs.setHandStrength(eHandStrength.ThreeOfAKind.getHandStrength());
				hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
				hs.setLoHand(0);
				ArrayList<Card> kickers = new ArrayList<Card>();
				kickers.add(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()));
				kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
				hs.setKickers(kickers);
			// Middle three are the same, while the end and first ones are not
			} else if (h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.FourthCard.getCardNo()).geteRank()) {
				bHandCheck = true;
				hs.setHandStrength(eHandStrength.ThreeOfAKind.getHandStrength());
				hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
				hs.setLoHand(0);
				ArrayList<Card> kickers = new ArrayList<Card>();
				kickers.add(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
				kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
				hs.setKickers(kickers);
			// Last three are the same while the first two are not
			} else if (h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
				bHandCheck = true;
				hs.setHandStrength(eHandStrength.ThreeOfAKind.getHandStrength());
				hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
				hs.setLoHand(0);
				ArrayList<Card> kickers = new ArrayList<Card>();
				kickers.add(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
				kickers.add(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()));
				hs.setKickers(kickers);
			}
		}
		
		return bHandCheck;
	}

	public static boolean isHandTwoPair(Hand h, HandScore hs) {
		boolean bHandCheck = false;
		//Makes sure that this is not a four of a kind (e.x. 2 2 2 2 4, in which both 2 and 2
		//pairs are the "same" but actually all four 2's are the same.
		if(!h.isHandFourOfAKind(h, hs)){
			//First and Second pair are the same, while last is not
			if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.SecondCard.getCardNo()).geteRank() &&
					h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.FourthCard.getCardNo()).geteRank()) {
				bHandCheck = true;
				hs.setHandStrength(eHandStrength.TwoPair.getHandStrength());
				hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
				hs.setLoHand(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr());
				ArrayList<Card> kickers = new ArrayList<Card>();
				kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
				hs.setKickers(kickers);
				//Second and Third pair are the same, while first is not
			} else if (h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.ThirdCard.getCardNo()).geteRank() &&
					h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
				bHandCheck = true;
				hs.setHandStrength(eHandStrength.TwoPair.getHandStrength());
				hs.setHiHand(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr());
				hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank().getiRankNbr());
				ArrayList<Card> kickers = new ArrayList<Card>();
				kickers.add(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
				hs.setKickers(kickers);
				//First and Third pair are the same, while middle is not
			} else if (h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.SecondCard.getCardNo()).geteRank() &&
					h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == h.getCardsInHand()
					.get(eCardNo.FifthCard.getCardNo()).geteRank()) {
				bHandCheck = true;
				hs.setHandStrength(eHandStrength.TwoPair.getHandStrength());
				hs.setHiHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
				hs.setLoHand(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank().getiRankNbr());
				ArrayList<Card> kickers = new ArrayList<Card>();
				kickers.add(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()));
				hs.setKickers(kickers);
			}
		}
		return bHandCheck;
	}

	public static boolean isHandPair(Hand h, HandScore hs) {
		boolean bHandCheck = false;
		int checkpoint = 0;
		ArrayList<Card> kickers = new ArrayList<Card>();
		
		//Makes sure the hand isn't a two pair and is also not a full house.
		if(!h.isHandTwoPair(h, hs) && !h.isHandFullHouse(h, hs)){
			//Loops through the hand, and figures out when the first two cards are 
			//the same, and marks where that "checkpoint" is
			for (int i = 0; i < 4; i++) {
				if (h.getCardsInHand().get(i).geteRank() ==
						h.getCardsInHand().get(i + 1).geteRank()) {
					checkpoint = i;
				}
			}
			//Loops through the hand again, but this time adding all cards other than
			//the two cards that are the same, into the kickers.
			for (int j = 0; j < 5; j++){
				if(j == checkpoint || j == checkpoint + 1){
					continue;
				}
				else{
					kickers.add(h.getCardsInHand().get(j));
				}
			}
			hs.setKickers(kickers);
			//High hand would be the pair's value, which is the checkpoint
			hs.setHiHand(h.getCardsInHand().get(checkpoint).geteRank().getiRankNbr());
			//Low hand is the highest value of the kicker's pile
			hs.setLoHand(h.getCardsInHand().get(kickers.size()-2).geteRank().getiRankNbr());
			hs.setHandStrength(eHandStrength.Pair.getHandStrength());
			bHandCheck = true;
		}
		return bHandCheck;
	}

	public static boolean isHandHighCard(Hand h, HandScore hs) {
		boolean bHandCheck = false;
		//If it's not any of the above hand methods, it becomes the high card.
		if(!h.isHandFiveOfAKind(h, hs) && !h.isHandFlush(h, hs) && !h.isHandFourOfAKind(h, hs) && !h.isHandFullHouse(h, hs)
				&& !h.isHandPair(h, hs) && !h.isHandRoyalFlush(h, hs) && !h.isHandStraight(h, hs) && !h.isHandStraightFlush(h, hs)
				&& !h.isHandThreeOfAKind(h, hs) && !h.isHandTwoPair(h, hs)){
			hs.setHandStrength(eHandStrength.HighCard.getHandStrength());
			bHandCheck = true;
			hs.setHiHand(h.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			hs.setLoHand(h.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
			ArrayList<Card> kickers = new ArrayList<Card>();
			kickers.add(h.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()));
			kickers.add(h.getCardsInHand().get(eCardNo.FourthCard.getCardNo()));
			kickers.add(h.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
			hs.setKickers(kickers);
		}
		return bHandCheck;
	}
//  .       .         
//  \`-"'"-'/
//   } 6 6 {       
//  =.  Y  ,=   
//(""-'***`-"")  
// `-/     \-'            
//  (  )-(  )===' 
//   ""   ""   
//MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW MEOW 

}

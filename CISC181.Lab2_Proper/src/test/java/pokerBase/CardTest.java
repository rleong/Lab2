package pokerBase;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pokerEnums.eRank;
import pokerEnums.eSuit;

public class CardTest {

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
	
	@Test
	public void ComparatorTest(){
		Card instance1 = new Card(pokerEnums.eSuit.CLUBS, pokerEnums.eRank.TWO, 0);
		Card instance2 = new Card(pokerEnums.eSuit.CLUBS, pokerEnums.eRank.THREE, 0);
		int expectedValue = 1;
		int actualValue = Card.CardRank.compare(instance1, instance2);
		//System.out.println(actualValue);
		
		assertEquals(expectedValue == actualValue, true);
	}
	
}

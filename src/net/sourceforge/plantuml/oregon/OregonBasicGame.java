/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 4639 $
 * 
 */
package net.sourceforge.plantuml.oregon;

public class OregonBasicGame implements BasicGame {

	private Screen screen;
	private SmartKeyboard kb;

	private final String da[] = new String[] { "March 29", "April 12", "April 26", "May 10", "May 24", "June 7",
			"June 21", "July 5", "July 19", "August 2", "August 16", "August 31", "September 13", "September 27",
			"October 11", "October 25", "November 8", "November 22", "December 6", "December 20" };
	private final int ep[] = new int[] { 6, 11, 13, 15, 17, 22, 32, 35, 37, 42, 44, 54, 64, 69, 95 };

	public Screen getScreen() {
		return screen;
	}

	private void print(String s) {
		screen.print(s);
	}

	private void print() {
		screen.print();
	}

	public void run(Keyboard keyboard) throws NoInputException {
		if (screen != null) {
			throw new IllegalStateException();
		}
		screen = new Screen();
		kb = new SmartKeyboard(keyboard);
		init();
	}

	private double rnd() {
		return 0.42;
	}

	private void init() throws NoInputException {
		printInitialScenario490();
		initialPurchasesOfPlayer690();
		initialShootingRanking920();
		screen.clear();
		print("<i>Your trip is about to begin...</i>");
		for (int j = 0; j < 20; j++) {
			print("<b>Monday, " + da[j] + ", 1847</b>. You are " + whereAreWe());
			print();
			if (f < 6) {
				print("<b>You're low on food. Better buy some or go hunting soon.</b>");
				print();
			}

			print("Total mileage to date is <b>" + m + "</b>");
			m += 200 + (a - 110) / 2.5 + 10 * rnd();
			print();
			// Calculate how far we travel in 2 weeks
			print("Here's what you now have (no. of bullets, $ worth of other items) :");
			printInventory3350();
			question1000(j);
			eating1310(j);
			montains2640();
			screen.clear();
		}
	}
	
	private int kp;

	private void montains2640() {
		if (m <= 975) {
			return;
		}
		final double mm = (m / 100.0 - 15);
		if (10 * rnd() > 9 - (mm * mm + 72) / (mm * mm + 12)) {

		}
// 2670 PRINT "You're in rugged mountain country." : IF RND(1) > .1 THEN 2700
//		2680 PRINT "You get lost and lose valuable time trying to find the trail."
//		2690 M = M - 60 : GOTO 2750
//		2700 IF RND(1) > .11 THEN 2730
//		2710 PRINT "Trail cave in damages your wagon. You lose time and supplies."
//		2720 M = M - 20 - 30 * RND(1) : B = B - 200 : R = R - 3 : GOTO 2750
//		2730 PRINT "The going is really slow; oxen are very tired." : M = M - 45 - 50 * RND(1)

	}
	
	private void southPass2750() {
		if (kp==1) {
			
		}
		kp = 1;
		if (rnd()<.8) {
			
		}
		
	}
	
//	2750 'South Pass routine
//	2760 IF KP = 1 THEN 2790 : 'Is the South Pass clear?
//	2770 KP = 1 : IF RND(1) < .8 THEN 2840 : '80% chance of blizzard
//	2780 PRINT "You made it safely through the South Pass....no snow!"
//	2790 IF M < 1700 THEN 2810
//	2800 IF KM = 1 THEN 2810 : 'Through Blue Mts yet?
//	2810 KM = 1 : IF RND(1) < .7 THEN 2840 ELSE RETURN : 'Get through without mishap?
//	2820 MP = 1 : RETURN : 'Set South Pass flag
//	2830 '
//	2840 PRINT "Blizzard in the mountain pass. Going is slow; supplies are lost."
//	2850 KB = 1 : M = M - 30 - 40 * RND(1) : F = F - 12 : B = B - 200 : R = R - 5
//	2860 IF C < 18 + 2 * RND(1) THEN GOTO 2880 ELSE RETURN : 'Enough clothes?
//	2870 '
	
	private int ks;

	private void dealWithIllness2880() {
		if (100 * rnd() < 10 + 35 * (e - 1)) {
			print("Mild illness. Your own medicine will cure it.");
			m -= 5;
			r -= 1;
			return;

		}
		if (100 * rnd() < 100.0 - 40.0 / Math.pow(4.0, e - 1)) {
			print("The whole family is sick. Your medicine will probably work okay.");
			m -= 5;
			r -= 2.5;
			return;

		}
		print("Serious illness in the family. You'll have to stop and see a doctor");
		print("soon. For now, your medicine will work.");
		r -= 5;
		ks = 1;
	}
	
	private void eating1310(int j) throws NoInputException {
		if (f < 5) {
			die3000(j);
			return;
		}
		do {
			print("Do you want to eat <b>(1)</b> poorly, <b>(2)</b> moderately or <b>(3)</b> well ?");
			e = kb.inputInt(screen);
			if (e < 1 || e > 3) {
				print("Enter 1, 2, or 3, please.");
				break;
			}
			final int ee = (int) (4 + 2.5 * e);
			if (e == 1 && ee > f) {
				f = 0;
				return;
			}
			if (ee > f) {
				print("You don't have enough to eat that well.");
				break;
			}
			f -= ee;
			return;
		} while (true);

	}
	private int kh;

	private void outOfMedicalSupplies3020(int j) throws NoInputException {
		print("You have run out of all medical supplies.");
		print();
		print("The wilderness is unforgiving and you die of ");
		if (kh == 1) {
			print("your injuries");
		} else {
			print("pneumonia");

		}
		print("Your family tries to push on, but finds the going too rough");
		print(" without you.");
		print3110(j);
	}

	
	private void die3000(int j) throws NoInputException {
		screen.clear();
		print("You run out of food and starve to death.");
		print();
		print3110(j);
	}

	private void print3110(int j) throws NoInputException {
		print("Some travelers find the bodies of you and your");
		print("family the following spring. They give you a decent");
		print("burial and notify your next of kin.");
		print();
		print("At the time of your unfortunate demise, you had been on the trail");
		final int d = 14 * j;
		final int dm = (int) (d / 30.5);
		final int dd = (int) (d - 30.5 * dm);
		print("for " + dm + " months and " + dd + " days and had covered " + (m + 70) + " miles.");
		print();
		print("You had a few supplies left :");
		printInventory3350();
		throw new NoInputException();
	}

	private void question1000(int j) throws NoInputException {
		int x;
		if (j % 2 == 1) {
			do {
				print("Want to <b>(1)</b> stop at the next fort, <b>(2)</b> hunt, or <b>(3)</b> push on ?");
				x = kb.inputInt(screen);
				if (x == 3) {
					return;
				}
			} while (x < 1 || x > 3);
		} else {
			do {
				print("Would you like to <b>(1)</b> hunt or <b>(2)</b> continue on ?");
				x = kb.inputInt(screen);
				if (x == 2) {
					return;
				}
			} while (x < 1 || x > 2);
		}

	}

	private void printInventory3350() {
		print("Cash Food Ammo Clothes Medicine, parts, etc.");
		if (f < 0) {
			f = 0;
		}
		if (b < 0) {
			b = 0;
		}
		if (c < 0) {
			c = 0;
		}
		if (r < 0) {
			r = 0;
		}
		print(" " + t + " " + f + " " + b + " " + c + " " + r);
		print();
	}

	private String whereAreWe() {
		if (m < 5) {
			return "on the high prairie.";
		}
		if (m < 200) {
			return "near Independence Crossing on the Big Blue River.";
		}
		if (m < 350) {
			return "following the Platte River.";
		}
		if (m < 450) {
			return "near Fort Kearney.";
		}
		if (m < 600) {
			return "following the North Platte River.";
		}
		if (m < 750) {
			return "within sight of Chimney Rock.";
		}
		if (m < 850) {
			return "near Fort Laramie.";
		}
		if (m < 1000) {
			return "close upon Independence Rock.";
		}
		if (m < 1050) {
			return "in the Big Horn Mountains.";
		}
		if (m < 1150) {
			return "following the Green River.";
		}
		if (m < 1250) {
			return "not too far from Fort Hall.";
		}
		if (m < 1400) {
			return "following the Snake River.";
		}
		if (m < 1550) {
			return "not far from Fort Boise.";
		}
		if (m < 1850) {
			return "in the Blue Mountains.";
		}
		return "following the Columbia River";

	}

	private void printInitialScenario490() {
		print("	Your journey over the Oregon Trail takes place in 1847.");
		print();
		print("Starting in Independence, Missouri, you plan to take your family of");
		print("five over 2040 tough miles to Oregon City.");
		print();
		print("	Having saved <b>$420</b> for the trip, you bought a wagon for <b>$70</b> and");
		print("now have to purchase the following items :");
		print();
		print(" * <b>Oxen</b> (spending more will buy you a larger and better team which");
		print("    will be faster so you'll be on the trail for less time)");
		print(" * <b>Food</b> (you'll need ample food to keep up your strength and health)");
		print(" * <b>Ammunition</b> ($1 buys a belt of 50 bullets. You'll need ammo for");
		print("    hunting and for fighting off attacks by bandits and animals)");
		print(" * <b>Clothing</b> (you'll need warm clothes, especially when you hit the");
		print("    snow and freezing weather in the mountains)");
		print(" * <b>Other supplies</b> (includes medicine, first-aid supplies, tools, and");
		print("    wagon parts for unexpected emergencies)");
		print();
		print(" You can spend all your money at the start or save some to spend");
		print("at forts along the way. However, items cost more at the forts. You");
		print("can also hunt for food if you run low.");
		print();

	}

	private void initialPurchasesOfPlayer690() throws NoInputException {
		if (kb.hasMore()) {
			screen.clear();
		}
		do {
			print("How much do you want to pay for a team of oxen ?");
			a = kb.inputInt(screen);
			if (a < 100) {
				print("No one in town has a team that cheap");
				continue;
			}
			break;
		} while (true);
		if (a >= 151) {
			print("You choose an honest dealer who tells you that $" + a + " is too much for");
			print("a team of oxen. He charges you $150 and gives you $" + (a - 150) + " change.");
			a = 150;
		}
		do {
			print();
			print("How much do you want to spend on food ?");
			f = kb.inputInt(screen);
			if (f <= 13) {
				print("That won't even get you to the Kansas River");
				print(" - better spend a bit more.");
				continue;
			}
			if (a + f > 300) {
				print("You wont't have any for ammo and clothes.");
				continue;
			}
			break;
		} while (true);
		do {
			print();
			print("How much do you want to spend on ammunition ?");
			b = kb.inputInt(screen);
			if (b < 2) {
				print("Better take a bit just for protection.");
				continue;
			}
			if (a + f + b > 320) {
				print("That won't leave any money for clothes.");
				continue;
			}
			break;
		} while (true);
		do {
			print();
			print("How much do you want to spend on clothes ?");
			c = kb.inputInt(screen);
			if (c <= 24) {
				print("Your family is going to be mighty cold in.");
				print("the montains.");
				print("Better spend a bit more.");
				continue;
			}
			if (a + f + b + c > 345) {
				print("That leaves nothing for medecine.");
				continue;
			}
			break;
		} while (true);
		do {
			print();
			screen.print("How much for medecine, bandage, repair parts, etc. ?");
			r = kb.inputInt(screen);
			if (r <= 5) {
				print("That's not at all wise.");
				continue;
			}
			if (a + f + b + c + r > 350) {
				print("You don't have that much money.");
				continue;
			}
			break;
		} while (true);
		t = 350 - a - f - b - c - r;
		print();
		print("You now have <b>$" + t + " left.</b>");
		b = 50 * b;
	}

	private void initialShootingRanking920() throws NoInputException {
		print();
		print("Please rank your shooting (typing) ability as follows :");
		print(" (1) Ace marksman  (2) Good shot  (3) Fair to middlin'");
		print(" (4) Need more practice  (5) Shaky knees");
		do {
			print();
			print("How do you rank yourself ?");
			dr = kb.inputInt(screen);
			if (dr >= 1 && dr <= 6) {
				return;
			}
			print("Please enter 1, 2, 3, 4 or 5.");
		} while (true);
	}

	private int e;
	private int a;
	private int b;
	private int f;
	private int c;
	private int r;
	private int t;
	private int dr;
	private int m;
}

package com.javainuse.chatbot.obrada_ulaznog_teksta;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class Amber {

	// Mapa koja definira odgovor na pitanje
	private  Map<String, List<String>> pitanjeOdgovor = new HashMap<>();


	public  boolean createLeadFlow;

	/*
	 * Definiranje jednog odgovora za svaku kategoriju upita.
	 */
	 {
		pitanjeOdgovor.put("greeting", Arrays.asList("Greetings, weary traveler. What is it that interests you?",
				"Hello, my name is Amber. What can I help you with?"));
		pitanjeOdgovor.put("product-inquiry",
				Arrays.asList("The product I sell is a insurance and saving plan. "
						));
		pitanjeOdgovor.put("price-inquiry", Arrays.asList(" It depend on the plan you buy."));
		pitanjeOdgovor.put("conversation-continue", Arrays.asList("Can I help you with anything else?"));
		pitanjeOdgovor.put("conversation-complete", Arrays.asList("It was pleasant, speaking with you, " +
				"but I am afraid our time together ends now.", "I think we are done talking. Good bye!"));
		pitanjeOdgovor.put("abuse-block", Arrays.asList("I don't answer to those kinds of words."));
		pitanjeOdgovor.put("other-product-inquiry", Arrays.asList("At this time, I sell only one product."));
		pitanjeOdgovor.put("aesthetic-of-product", Arrays.asList("Insurance are *very* pretty, and I assure every customer of mine" +
				" that they will definitely only make their space a more indulgent aesthetic experience."));
		pitanjeOdgovor.put("purpose-of-product", Arrays.asList("Its aim is to reduce financial uncertainty and make accidental loss manageable. It does this substituting payment of a small, known fee—an insurance premium—to a professional insurer in exchange for the assumption of the risk a large loss, and a promise to pay in the event of such a loss.\n"));
		pitanjeOdgovor.put("faq-reminder", Arrays.asList("We will send you reminders before debiting your account to ensure that you have sufficient balance."));
		pitanjeOdgovor.put("faq-change-mode", Arrays.asList(" Yes, a change in premium payment mode is allowed on the policy anniversary for active policies. You can avail this option by sending us a signed form."));
		pitanjeOdgovor.put("faq-payment-modes", Arrays.asList("We offer you several convenient options to make your premium payments:\n" +
				"\n" +
				"a. Through ECS which is available across all modes, i.e., monthly, quarterly, half-yearly or annually.\n" +
				"\n" +
				"b. Through drop boxes at various locations in your city.\n" +
				"\n" +
				"c. Through IVR - call helpline number 1860 120 5577\n" +
				"\n" +
				"d. Through the internet.\n" +
				"\n" +
				"e. At all our branch locations in your city."));
		pitanjeOdgovor.put("faq-payment-collection", Arrays.asList("Yes, we can get your premium payment collected from your house within 7 days from the date when you place a request with us (please note this facility is available only in selected cities.). A request for payment collection can be raised by calling us on our customer helpline toll free number 1860 120 5577 or sending us an email at service.helpdesk@maxlifeinsurance.com"));
		pitanjeOdgovor.put("faq-term-plan", Arrays.asList("Well, term insurance, just like other forms of insurance, is a contract between the insurance company and a buyer. As per this contract, the insurance company collects regular premiums from the policy buyer. In case the policyholder passes away within the policy period, the family of the policyholder receives a pre-determined amount of money.\n" +
				"\n"));
		pitanjeOdgovor.put("faq-saving-plan", Arrays.asList("You may have many goals in life for which you save money. These may be short-term goals, or long-term goals, such as buying a sedan, planning for children’s education in an ivy college, and planning for your daughter’s destination wedding. To ensure that you meet these goals, you need a financial instrument that can systematically grow your savings.\n" +
				"\n"));

		pitanjeOdgovor.put("faq-nominee-change", Arrays.asList("a.  Nomination change: You can make or change nomination anytime during the benefit period of active policies or just by filling out the policy amendment request form. In case, you have not effect a nomination during policy application, you must effect a nomination for easy settlement of claims.\n" +
				"\n" +
				"b.  Address change: You can change your address by sending a filled out policy amendment request form along with your address proof (if cumulative premium of all your active policies is greater than ₹10,000 inclusive of all applicable taxes, cess, and levies as imposed by the Government)."));
	pitanjeOdgovor.put("faq-policy-loan", Arrays.asList("If your policy has a cash surrender value, you can avail a policy loan of up to 90% of the cash surrender value subject to a minimum availability of ₹10,000. The policy loan can be taken for active policies (other than the ones in grace period), any time after completion of three policy years.\n" +
			"\n" +
			"Please note that policy loan is not available for Term Plans and Life Maker Unit Link Plan.\n" +
			"\n"));

	pitanjeOdgovor.put("faq-policy-loan", Arrays.asList("If your policy has a cash surrender value, you can avail a policy loan of up to 90% of the cash surrender value subject to a minimum availability of ₹10,000. The policy loan can be taken for active policies (other than the ones in grace period), any time after completion of three policy years.\n" +
			"\n" +
			"Please note that policy loan is not available for Term Plans and Life Maker Unit Link Plan.\n" +
			"\n"));
	pitanjeOdgovor.put("faq-lapsed-policy", Arrays.asList("The policy can be revived within the revival period as prescribed in the policy contract, if you send us a filled out health declaration form, and provide evidence of insurability acceptable to us as per our underwriting practices. All your overdue premiums and unpaid charges should be paid, if any\n" +
			"\n"));

	pitanjeOdgovor.put("faq-rider-update", Arrays.asList("Yes, you can add riders by sending us a filled policy amendment request form, and submitting the required documents, subject to prevailing underwriting rules. Deletion of rider is also allowed. You may send us a filled policy amendment request form for deleting a rider. For details, please contact your insurance advisor, or email us at service.helpdesk@maxlifeinsurance.com.\n" +
			"\n"));

	pitanjeOdgovor.put("faq-life-cover-amount-decision", Arrays.asList("The life cover you need depends on your standard of living, income, spending habits, or the goal for which you want to save (i.e. your child’s education or marriage) etc. As the life cover amount acts as a financial security for your family/beneficiaries in your absence, it becomes even more important for you to take an appropriate amount of life cover also taking your current financial liabilities (loans etc.) into consideration."));
	pitanjeOdgovor.put("faq-life-payment-miss-scenario", Arrays.asList("You usually get a grace period, up to 30 days (15 days for monthly mode), to pay your premium once it falls overdue. If you still don’t pay your premium after the grace period your policy stands defunct and you cannot claim any benefits from your policy. However, you can revive your policy once you pay all your overdue premiums subject to certain terms and conditions as per policy and you will again start receiving the benefits of the policy.\n" +
			"\n"));

	pitanjeOdgovor.put("faq-insurace-stop-impact", Arrays.asList(" If you stop premium payments of your policy, it amounts to discontinuation of the policy and you cannot claim any tax benefits. However, if you discontinue paying your premiums after 2 years from the commencement of your policy, the tax will not be deducted on the premium paid in the year when your policy ends. The amount of tax deducted on the premium paid in the preceding year is taxable in the year when the policy terminates."));

	pitanjeOdgovor.put("faq-different-types-plan", Arrays.asList(" Max Life Smart Term Plan, Max Life Smart Term Plan,Unit linked insurance plan"));

	pitanjeOdgovor.put("provide_lead_information", Arrays.asList(("Great! To help you better, I need some information. ")));

pitanjeOdgovor.put("faq-service-on-mobile", Arrays.asList("Yes, you may access the following services over our helpline number 1860 120 5577 :\n" +
		"\n" +
		"a. Change of address (if the annual is less than ₹10,000 including all applicable taxes, cess, and levies as imposed by the Government)\n" +
		"\n" +
		"b. Change of bonus options\n" +
		"\n" +
		"c. Change of contact details\n" +
		"\n" +
		"d. Change of premium payment mode (only for conventional policies)\n" +
		"\n" +
		"e. Reinstatements within 90 days\n" +
		"\n" +
		"Exercise of OPPB and illustrations"));
	}



	public  String start(WebSocketSession session, String msg) throws FileNotFoundException, IOException, InterruptedException {

		Kategorizator kategorizator = new Kategorizator();

		boolean razgovorJeGotov = false;
//		while (true) {

			String dodatneInformacije = "\n";


			// Razlama string unosKorisnika na rečenice
			DetektorRecenica detektor = new DetektorRecenica();
			String[] sentences = detektor.razlomiTesktNaRecenice(msg);
			dodatneInformacije += "Sentence Detection: " + Arrays.stream(sentences).collect(Collectors.joining(" | ")) + "\n";

			String odgovor = "";
			Pattern DATE_PATTERN = Pattern.compile(
					"^(?:(?:0[1-9]|1[0-2])[-/](?:0[1-9]|[12][0-9]|3[01])[-/](?:\\d{4}|\\d{2}))|(?:(?:0[1-9]|[12][0-9]|3[01])[-/](?:0[1-9]|1[0-2])[-/](?:\\d{4}|\\d{2}))|(?:(?:\\d{4}|\\d{2})[-/](?:0[1-9]|1[0-2])[-/](?:0[1-9]|[12][0-9]|3[01]))$");


			String mobilePattern = "\\b\\d{10}\\b";
			String salaryPattern = "\\b\\d+\\b";
			String genderPattern = "^[MFmf]$";
			String smokerPattern = "^(?i)(Y|N|Yes|No|T|F|Smoker|Non-Smoker)$";

			// Program u petlji obrađuje rečenicu po rečenicu
			for (String sentence : sentences) {

				// Riječi u rečenici se odjeljuju jedna od druge koristeći tokenizer.
				Tokenizer tokenizer = new Tokenizer();
				String[] tokeni = tokenizer.tokeniziraj(sentence);
				dodatneInformacije += "Tokenizer : " + Arrays.stream(tokeni).collect(Collectors.joining(" | ")) + "\n";

				// Odvojene riječi se označavaju sa POS tagovima
				POS_Tagger posTagger = new POS_Tagger();
				String[] posTags = posTagger.tag(tokeni);
				dodatneInformacije += "POS Tags : " + Arrays.stream(posTags).collect(Collectors.joining(" | ")) + "\n";

				// Savaka riječ se lematizira kako bi ju bilo lakše kategorizirati
				Lematizator lematizator  = new Lematizator();
				String[] lemmaTokens = lematizator.lematiziraj(tokeni, posTags);
				dodatneInformacije += "Lemmatizer : " + Arrays.stream(lemmaTokens).collect(Collectors.joining(" | ")) + "\n";

				// Pronalazak najbolje kategorije u koju input rečenica spada
				String category = kategorizator.vratiKategoriju(lemmaTokens);
				dodatneInformacije += "Category: " + category + "\n";

				// Provjeravamo je li kategorija jednaka onoj za završetak razgovora
				if ("conversation-complete".equals(category)) razgovorJeGotov = true;



				// Odgovor se determinira iz kategorije.
				List<String> listaOdgovora = pitanjeOdgovor.get(category);
				Random random = new Random();
				odgovor = odgovor + " " + listaOdgovora.get(random.nextInt(listaOdgovora.size()));

			}

			//Isprintaj dodatne informacije
			System.out.println(dodatneInformacije);

			// Isprintaj odgovor chatbota Amber u konzolu
		session.sendMessage(new TextMessage("▁ ▂ ▄ ▅ ▆ ▇ █ \uD835\uDD38\uD835\uDD5E\uD835\uDD53\uD835\uDD56\uD835\uDD63 █ ▇ ▆ ▅ ▄ ▂ ▁ "));
			session.sendMessage(new TextMessage(odgovor));

			//Provjeri je li razgovor gotov
//			if (razgovorJeGotov) break;
//		}
		return "";

	}



//	public static String start(String msg) throws FileNotFoundException, IOException, InterruptedException {
//
//		// Stvori kategorizator
//		Kategorizator kategorizator = new Kategorizator();
//
//		// Čitaj unos korisnika sa konzole u while(true) petlji dok se zastavica za završetak razgovora ne podigne
//		Scanner scanner = new Scanner(System.in);
//		boolean razgovorJeGotov = false;
//		while (true) {
//
//			String dodatneInformacije = "\n";
//
//			// Unos korisnika
////			System.out.println("▁ ▂ ▄ ▅ ▆ ▇ █ \uD835\uDD50\uD835\uDD60\uD835\uDD66 █ ▇ ▆ ▅ ▄ ▂ ▁");
////			String unosKorisnika = scanner.nextLine();
//
//			// Razlama string unosKorisnika na rečenice
//			DetektorRecenica detektor = new DetektorRecenica();
//			String[] sentences = detektor.razlomiTesktNaRecenice(msg);
//			dodatneInformacije += "Sentence Detection: " + Arrays.stream(sentences).collect(Collectors.joining(" | ")) + "\n";
//
//			String odgovor = "";
//			Pattern DATE_PATTERN = Pattern.compile(
//					"^(?:(?:0[1-9]|1[0-2])[-/](?:0[1-9]|[12][0-9]|3[01])[-/](?:\\d{4}|\\d{2}))|(?:(?:0[1-9]|[12][0-9]|3[01])[-/](?:0[1-9]|1[0-2])[-/](?:\\d{4}|\\d{2}))|(?:(?:\\d{4}|\\d{2})[-/](?:0[1-9]|1[0-2])[-/](?:0[1-9]|[12][0-9]|3[01]))$");
//
//
//			String mobilePattern = "\\b\\d{10}\\b";
//			String salaryPattern = "\\b\\d+\\b";
//			String genderPattern = "^[MFmf]$";
//			String smokerPattern = "^(?i)(Y|N|Yes|No|Smoker|Non-Smoker)$";
//
//			// Program u petlji obrađuje rečenicu po rečenicu
//			for (String sentence : sentences) {
//
//				// Riječi u rečenici se odjeljuju jedna od druge koristeći tokenizer.
//				Tokenizer tokenizer = new Tokenizer();
//				String[] tokeni = tokenizer.tokeniziraj(sentence);
//				dodatneInformacije += "Tokenizer : " + Arrays.stream(tokeni).collect(Collectors.joining(" | ")) + "\n";
//
//				// Odvojene riječi se označavaju sa POS tagovima
//				POS_Tagger posTagger = new POS_Tagger();
//				String[] posTags = posTagger.tag(tokeni);
//				dodatneInformacije += "POS Tags : " + Arrays.stream(posTags).collect(Collectors.joining(" | ")) + "\n";
//
//				// Savaka riječ se lematizira kako bi ju bilo lakše kategorizirati
//				Lematizator lematizator  = new Lematizator();
//				String[] lemmaTokens = lematizator.lematiziraj(tokeni, posTags);
//				dodatneInformacije += "Lemmatizer : " + Arrays.stream(lemmaTokens).collect(Collectors.joining(" | ")) + "\n";
//
//				// Pronalazak najbolje kategorije u koju input rečenica spada
//				String category = kategorizator.vratiKategoriju(lemmaTokens);
//				dodatneInformacije += "Category: " + category + "\n";
//
//				// Provjeravamo je li kategorija jednaka onoj za završetak razgovora
//				if ("conversation-complete".equals(category)) razgovorJeGotov = true;
//
//				while (createLeadFlow) {
//					try {
//
//
//
//						System.out.println("Bot: Please enter dob");
//						Scanner scanner1 = new Scanner(System.in);
//						String dob = scanner1.nextLine();
//						Matcher dobMatcher = DATE_PATTERN.matcher(dob);
//						while (createLeadFlow && !dobMatcher.find()) {
//							System.out.println("Bot: Please enter valid dob or press q to exit");
//							String dobStr = new Scanner(System.in).nextLine();
//							if (dobStr.equalsIgnoreCase("Q"))
//								createLeadFlow = false;
//							dob = dobStr;
//						}
//						System.out.println("Bot: Please enter salary in number");
//						int salary = new Scanner(System.in).nextInt();
//
//						System.out.println("Bot: Please enter gender");
//
//						String gender = new Scanner(System.in).nextLine();
//						Pattern regex = Pattern.compile(genderPattern);
//						Matcher matcher = regex.matcher(gender);
//
//						while (createLeadFlow && !matcher.find()) {
//							System.out.println("Bot: Please enter valid gender value or press q to exit");
//							String data = new Scanner(System.in).nextLine();
//							if (data.equalsIgnoreCase("Q"))
//								createLeadFlow = false;
//							gender=data;
//						}
//						gender = matcher.group(0);
//
//						System.out.println("Bot: Please enter mobile");
//						String mobile = new Scanner(System.in).nextLine();
//
//						Pattern mobileRegex = Pattern.compile(mobilePattern);
//						Matcher matcher1 = mobileRegex.matcher(mobile);
//
//
//						while (createLeadFlow && !matcher1.find()) {
//							System.out.println("Bot: Please enter valid mobile value or press q to exit");
//							String data = new Scanner(System.in).nextLine();
//							if (data.equalsIgnoreCase("Q"))
//								createLeadFlow = false;
//							mobile=data;
//						}
//						mobile = matcher1.group(0);
//
//						System.out.println("Bot: Please enter if you are smoker or not");
//
//						String isSmoker = new Scanner(System.in).nextLine();
//
//						Pattern smokerPttrn = Pattern.compile(smokerPattern);
//						Matcher smokerMatcher = smokerPttrn.matcher(isSmoker);
//
//
//						while (createLeadFlow && !smokerMatcher.find()) {
//							System.out.println("Bot: Please enter valid smoker value or press q to exit");
//							String data = new Scanner(System.in).nextLine();
//							if (data.equalsIgnoreCase("Q"))
//								createLeadFlow = false;
//							isSmoker=data;
//						}
//						isSmoker = smokerMatcher.group(0);
//
//						createLeadFlow=false;
//
//						System.out.println("Bot: Thanks for details");
//
//					}
//				catch(Exception e){
//					System.out.println("Bot: Please enter valid  values or press q to exit");
//					String data = new Scanner(System.in).nextLine();
//					if (data.equalsIgnoreCase("Q"))
//						createLeadFlow = false;
//					}
//				}
//
//
//				// Odgovor se determinira iz kategorije.
//				List<String> listaOdgovora = pitanjeOdgovor.get(category);
//				Random random = new Random();
//				odgovor = odgovor + " " + listaOdgovora.get(random.nextInt(listaOdgovora.size()));
//				if (category.equals("provide_lead_information")){
//					createLeadFlow=true;
//				}
//			}
//
//			//Isprintaj dodatne informacije
//			System.out.println(dodatneInformacije);
//
//			// Isprintaj odgovor chatbota Amber u konzolu
//			System.out.println("▁ ▂ ▄ ▅ ▆ ▇ █ \uD835\uDD38\uD835\uDD5E\uD835\uDD53\uD835\uDD56\uD835\uDD63 █ ▇ ▆ ▅ ▄ ▂ ▁ ");
//			System.out.println(odgovor);
//
//			//Provjeri je li razgovor gotov
//			if (razgovorJeGotov) break;
//		}
//
//	}

}

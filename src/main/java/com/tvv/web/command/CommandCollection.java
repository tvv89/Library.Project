package com.tvv.web.command;

import com.tvv.web.command.admin.*;
import com.tvv.web.command.incognito.CreateUserCommand;
import com.tvv.web.command.incognito.LoadListIncognitoBooksCommand;
import com.tvv.web.command.incognito.RegistrationCommand;
import com.tvv.web.command.librarian.*;
import com.tvv.web.command.user.*;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

/**
 * Command collector. Collect all command for POST and GET request from user
 */
public class CommandCollection {
	
	private static final Logger log = Logger.getLogger(CommandCollection.class);
	
	private static Map<String, Command> commands = new TreeMap<String, Command>();
	
	static {
		/**
		 * Main authentication commands
		 */
		commands.put("login", new LoginCommand());
		commands.put("logout", new LogoutCommand());
		commands.put("registration", new RegistrationCommand());
		commands.put("createUser", new CreateUserCommand());
		/**
		 * Command for word with Admin access
		 */
		commands.put("listUsers", new LoadListAdminUsersCommand());
		commands.put("listBooks", new LoadListAdminBooksCommand());
		commands.put("listLibrarians", new LoadListAdminLibrariansCommand());
		commands.put("updateListUser", new UpdateListAdminUsersCommand());
		commands.put("updateListBook", new UpdateListAdminBooksCommand());
		commands.put("updateListLibrarians", new UpdateListAdminLibrariansCommand());
		commands.put("statusUser", new StatusUsersCommand());
		commands.put("createBook", new CreateBookCommand());
		commands.put("createLibrarian", new CreateLibrarianCommand());
		commands.put("updateBook", new UpdateBookCommand());
		commands.put("infoBook", new InfoBookCommand());
		commands.put("deleteBook", new DeleteBookCommand());
		commands.put("loadBookImage", new ImageBookLoadCommand());
//		commands.put("roleUser", new UpdateUserRoleCommand());
		/**
		 * Command for word with Librarian
		 */
		commands.put("listLibrarianRentBooks", new LoadListLibrarianRentBooksCommand());
		commands.put("updateRentBook", new UpdateListLibrarianRentBooksCommand());
		commands.put("infoUser", new InfoUserCommand());
		commands.put("giveBookForUser", new GiveBookForUserCommand());
		commands.put("payFineBookForUser", new PayFineForUserCommand());
		commands.put("returnBookToLibrary", new ReturnBookToLibraryCommand());

		commands.put("listLibrarianAllBooks", new LoadListLibrarianBooksCommand());
		commands.put("updateListLibrarianBooks", new UpdateListLibrarianBooksCommand());
		commands.put("takeLibrarianBook", new GiveLibrarianBookCommand());

		commands.put("listLibrarianUsers", new LoadListLibrarianUserCommand());
		commands.put("updateListLibrarianUsers", new UpdateListLibrarianUsersCommand());

		/**
		 * Command for word with User
		 */
		commands.put("listUserAllBooks", new LoadListUserBooksCommand());
		commands.put("updateListUserBooks", new UpdateListUserBooksCommand());

		commands.put("listUserRentBooks", new LoadListUserRentBooksCommand());
		commands.put("updateUserRentBook", new UpdateListUserRentBooksCommand());
		commands.put("takeUserBook", new TakeUserBookCommand());

		/**
		 * Command for word with incognito
		 */
		commands.put("listIncognitoAllBooks", new LoadListIncognitoBooksCommand());
		commands.put("updateListIncognitoBooks", new UpdateListLibrarianBooksCommand());

		/**
		 * Command for word with Payments
		 */
//		commands.put("listPayments", new LoadListPaymentsCommand());
//		commands.put("updateListPayment", new UpdateListPaymentsCommand());
//		commands.put("infoPayment", new InfoPaymentCommand());
//		commands.put("statusPayment", new StatusPaymentsCommand());
//		commands.put("createPayment", new CreatePaymentCommand());

//		commands.put("noCommand", new WrongAttributeCommand());
		commands.put("language", new LanguageCommand());
		log.debug("Command container was initialized");
		log.trace("Number of commands: " + commands.size());
	}

	/**
	 * Get command by name
	 * @param commandName String command name
	 * @return object Command
	 */
	public static Command get(String commandName) {
		if (commandName == null || !commands.containsKey(commandName)) {
			log.trace("Command not found: " + commandName);
			return commands.get("noCommand"); 
		}
		
		return commands.get(commandName);
	}
	
}
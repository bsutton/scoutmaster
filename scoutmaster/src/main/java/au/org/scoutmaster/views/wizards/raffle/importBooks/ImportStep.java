package au.org.scoutmaster.views.wizards.raffle.importBooks;

import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RaffleBookDao;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleBook;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ImportStep implements WizardStep
{
	private RaffleBookImportWizardView wizard;

	public ImportStep(RaffleBookImportWizardView setupWizardView)
	{
		this.wizard = setupWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Import Complete";
	}

	@Override
	public Component getContent()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();
		
		Raffle raffle = this.wizard.getRaffle();
		TicketRangeStep ticketRange = this.wizard.getTicketRangeStep();
		
		
		
		int firstBookNo = ticketRange.getFirstBookNo();
		int lastBookNo = ticketRange.getLastBookNo();
		int ticketsInBook = raffle.getTicketsPerBook();
		
		int currentBookNo = firstBookNo;
		for (currentBookNo = firstBookNo; currentBookNo < lastBookNo; currentBookNo += ticketsInBook)
		{
			RaffleBook book = new RaffleBook();
			book.setRaffle(raffle);
			book.setFirstNo(currentBookNo);
			book.setTicketCount(ticketsInBook);
			daoRaffleBook.persist(book);
		}
		
		Label label = new Label("<h1>Import Complete</h1>"
				+ "<p>" + ticketRange.getBookCount() + " books have been imported and can be viewed under the 'Books' tab on the Raffle page.</p>");
		label.setContentMode(ContentMode.HTML);
		layout.addComponent(label);

		return layout;
	}

	@Override
	public boolean onAdvance()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onBack()
	{
		// Can't go back now as the books have been imported.
		// TODO: consider deleting the imported books if we click back.
		return false;
	}

	
}

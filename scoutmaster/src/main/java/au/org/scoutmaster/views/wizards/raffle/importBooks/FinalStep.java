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

public class FinalStep implements WizardStep
{
	private RaffleBookImportWizardView wizard;

	public FinalStep(RaffleBookImportWizardView setupWizardView)
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
		
		StringBuffer sb = new StringBuffer();
		sb.append("<h1>Import Complete</h1>");
		sb.append("<p>" + ticketRange.getBookCount() + " books have been imported and can be viewed under the 'Books' tab on the Raffle page.</p>");
		sb.append("<p></p><p>If you have more books to import simply run the import wizard again.</p>");
		
		Label label = new Label(sb.toString());
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

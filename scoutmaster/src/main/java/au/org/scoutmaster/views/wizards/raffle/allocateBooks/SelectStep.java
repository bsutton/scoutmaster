package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.util.List;

import org.vaadin.teemu.wizards.WizardStep;

import au.org.scoutmaster.domain.Contact;

public interface SelectStep extends WizardStep
{

	Contact getIssuedByContact();

	List<Allocation> getAllocations();

}

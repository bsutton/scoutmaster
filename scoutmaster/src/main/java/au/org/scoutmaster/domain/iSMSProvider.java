package au.org.scoutmaster.domain;

public interface iSMSProvider
{
	/**
	 * 
	 * @param phone - the phone no. to send the message to. Must be of type Mobile.
	 * @param msg - the message to be sent.
	 */
	public void send(Phone phone, String msg);

	public void init();

}

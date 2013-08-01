package au.org.scoutmaster.editors;


public interface DialogListener<T>
{
	void confirmed(T details);
	
	void declined();

}

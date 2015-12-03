package au.org.scoutmaster.domain;

public interface Importable
{
	// Allow an importable entity to be tagged as part of the import process.
	void addTag(Tag tag);

}

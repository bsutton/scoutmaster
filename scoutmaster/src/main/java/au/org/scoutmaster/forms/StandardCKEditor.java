package au.org.scoutmaster.forms;

import org.vaadin.openesignforms.ckeditor.CKEditorConfig;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;

import com.vaadin.ui.VerticalLayout;

public class StandardCKEditor extends VerticalLayout
{
	private static final long serialVersionUID = 1L;
	private CKEditorTextField ckEditorTextField;

	public StandardCKEditor()
	{

		CKEditorConfig config = new CKEditorConfig();
		config.useCompactTags();
		config.disableElementsPath();
		config.setResizeEnabled(false);
		config.setToolbarCanCollapse(false);
		config.disableResizeEditor();


		String toolbarLineJS = " "
				+ "{ name: 'document', items : [ 'Source','-','Save','NewPage','DocProps','Preview','Print','-','Templates' ] },"
				+ "{ name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },"
				+ "{ name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt' ] },"
				+ "'/',"
				+ "{ name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },"
				+ "{ name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },"
				+ "{ name: 'links', items : [ 'Link','Unlink','Anchor' ] },"
				+ "'/'," 
				+ "{ name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },"
				+ "{ name: 'colors', items : [ 'TextColor','BGColor' ] },"
				+ "{ name: 'tools', items : [ 'Maximize', 'ShowBlocks','-','About' ] }" 
				+ "";

		config.addCustomToolbarLine(toolbarLineJS);
//		config.addCustomToolbarLine("[ { name: 'document', items : [ 'Source','-','Save','NewPage','DocProps','Preview','Print','-','Templates' ] } ]");
//		config.addCustomToolbarLine("[ ['Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink','-','About']]");
//		config.addCustomToolbarLine("{ name: 'styles', items : ['Styles','Format','Font','FontSize','TextColor','BGColor','Maximize', 'ShowBlocks','-','About'] }");

		ckEditorTextField = new CKEditorTextField(config);
		ckEditorTextField.setWidth("100%");
		ckEditorTextField.setHeight("100%");

		this.addComponent(ckEditorTextField);
		this.setSizeFull();

	}
	
	public String getValue()
	{
		return ckEditorTextField.getValue();
	}
	
	public void setReadOnly(boolean readOnly)
	{
		ckEditorTextField.setReadOnly(readOnly);
	}

	public void setValue(String body)
	{
		ckEditorTextField.setValue(body);
		
	}

}

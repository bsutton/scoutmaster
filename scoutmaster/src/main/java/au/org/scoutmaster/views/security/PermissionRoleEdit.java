package au.org.scoutmaster.views.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.metamodel.SingularAttribute;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CheckBox;

import au.com.vaadinutils.crud.ChildCrudListener;
import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.security.Permission;
import au.org.scoutmaster.domain.security.SecurityRole;
import au.org.scoutmaster.domain.security.SecurityRole_;
import au.org.scoutmaster.security.eSecurityRole;

public class PermissionRoleEdit implements ChildCrudListener<Permission>
{

	Map<String, CheckBox> roleCheckBoxes = new HashMap<>();
	Map<String, Boolean> initialValues = new HashMap<>();

	PermissionRoleEdit(AbstractLayout layout)
	{
		@SuppressWarnings("unchecked")
		List<SecurityRole> roles = DaoFactory.getGenericDao(SecurityRole.class).findAll(new SingularAttribute[]
		{ SecurityRole_.erole });
		for (SecurityRole role : roles)
		{
			CheckBox cb = new CheckBox(role.getName());
			roleCheckBoxes.put(role.getName(), cb);

			layout.addComponent(cb);
		}
	}

	@Override
	public void committed(Permission newEntity) throws Exception
	{
		JpaBaseDao<SecurityRole, Long> roleDao = DaoFactory.getGenericDao(SecurityRole.class);

		Set<SecurityRole> existingRoles = newEntity.getRoles();
		existingRoles.clear();

		for (Entry<String, CheckBox> role : roleCheckBoxes.entrySet())
		{
			if (role.getValue().getValue())
			{
				existingRoles.add(roleDao.findOneByAttribute(SecurityRole_.erole, eSecurityRole.valueOf(role.getKey())));
			}
		}

	}

	@Override
	public void selectedParentRowChanged(EntityItem<Permission> parent)
	{
		if (parent != null && parent.getEntity() != null)
		{
			Set<String> groups = new HashSet<>();
			for (SecurityRole group : parent.getEntity().getRoles())
			{
				groups.add(group.getName());
			}
			for (Entry<String, CheckBox> group : roleCheckBoxes.entrySet())
			{
				final boolean isGroupSelected = groups.contains(group.getKey());
				group.getValue().setValue(isGroupSelected);
				initialValues.put(group.getKey(), isGroupSelected);
			}
		}
	}

	@Override
	public boolean isDirty()
	{
		for (Entry<String, CheckBox> group : roleCheckBoxes.entrySet())
		{
			if (group.getValue().getValue() != initialValues.get(group.getKey()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void validateFieldz()
	{

	}

	@Override
	public void discard()
	{

	}

	@Override
	public void saveEditsToTemp() throws Exception
	{
	}

	@Override
	public boolean interceptSaveClicked()
	{
		return true;
	}
}
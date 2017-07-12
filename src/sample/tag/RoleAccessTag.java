package sample.tag;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import sample.model.UserCredentials;

public class RoleAccessTag extends TagSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roleRequired;
	
	public void setRoleRequired(String roleRequired){
		this.roleRequired = roleRequired;
	} 
	
	@Override
	public int doStartTag() throws JspException {
		pageContext.getSession();
		
		UserCredentials user = (UserCredentials) pageContext.getSession().getAttribute("UserCred");
		
		if(user.hasUserRoleName(roleRequired))
			return EVAL_BODY_INCLUDE;
		
		// else
		return SKIP_BODY;
		
	} 
	

}

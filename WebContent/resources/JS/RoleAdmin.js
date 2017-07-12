var userAjax = new XMLHttpRequest();

var data = null;

userAjax.onreadystatechange = function(){
	if(userAjax.readyState == XMLHttpRequest.DONE && userAjax.status == 200){
		data = JSON.parse(userAjax.responseText);

		fillUserTable(data.users);
	}
}

userAjax.open("GET", "/ApplicationDirectoryBasic/role");
userAjax.send();

var userToEdit = null;

function editRoles(rowId) {
	var select = document.getElementById("selectMod");
	
	var username = rowId.substr(0, rowId.length - 3);
	
	// Grab data for user selected
	var user = data.users.filter(function (f) { return username == f.username})[0];
	userToEdit = {};
	
	Object.keys(user).forEach(function(elt) {
		if(elt != "userroles")
			userToEdit[elt] = user[elt];
	});
	
	userToEdit.userroles = [];
	user.userroles.forEach(function(e) {
		userToEdit.userroles.push(e);
	});
	
	var uRoles = getRolesNotSet(data.roles);
	
	// Move data into bottom box and activate it
	document.getElementById("usernameMod").innerHTML = username;
	fillRolesTable(userToEdit.userroles);
	fillSelect(uRoles);
	
	select.disabled = false;
	
	document.getElementById("addRoleButton").disabled = false;
	document.getElementById("cancel").disabled = false;
}

function addRole(){
	var select = document.getElementById("selectMod");
	
	var selectedRole = select.options[select.selectedIndex].text;
	
	// Remove role from select
	select.remove(select.selectedIndex);
	
	// Add Role to user
	userToEdit.userroles.push(data.roles.filter(function(r){
		return r.rolename == selectedRole;
	})[0]);
	
	userToEdit.userroles.sort(compareRoles);
	
	// Add Role to table
	fillRolesTable(userToEdit.userroles);
	
	var user = data.users.filter(function (f) { return userToEdit.username == f.username})[0];
	
	
	if(hasRoleChanges(userToEdit.userroles, user.userroles))
		document.getElementById("save").disabled = false;
	else
		document.getElementById("save").disabled = true;
	
	if(select.options.length == 0){
		select.disabled = true;
		document.getElementById("addRoleButton").disabled = true;
	}
	else{
		select.disabled = false;
		document.getElementById("addRoleButton").disabled = false;
	}

}

function removeRole(role) {
	var select = document.getElementById("selectMod");
		
	// Remove role from table
	document.getElementById("modTableBody").removeChild(document.getElementById(role+"Row"));
	
	// Remove role from user
	for(var i = 0; i < userToEdit.userroles.length; i++){
		if(userToEdit.userroles[i].rolename == role){
			userToEdit.userroles.splice(i, 1);
			break;
		}
	}
	
	var uRoles = getRolesNotSet(data.roles);
	
	fillSelect(uRoles);
	
	var user = data.users.filter(function (f) { return userToEdit.username == f.username})[0];
	
	if(hasRoleChanges(userToEdit.userroles, user.userroles))
		document.getElementById("save").disabled = false;
	else
		document.getElementById("save").disabled = true;
	
	select.disabled = false;
	document.getElementById("addRoleButton").disabled = false;
}

function clearEditBox(){
	userToEdit = null;
	
	document.getElementById("usernameMod").innerHTML = "";
	document.getElementById("selectMod").innerHTML = "";
	document.getElementById("modTableBody").innerHTML = "";
	
	document.getElementById("selectMod").disabled = true;
	document.getElementById("addRoleButton").disabled = true;
	document.getElementById("save").disabled = true;
	document.getElementById("cancel").disabled = true;
	
}

function save() {
	if(userToEdit != null){
		
		
		var user = data.users.filter(function (f) { return userToEdit.username == f.username})[0];
		
		// Figure out which roles to add
		var rolesAdded = userToEdit.userroles.filter(function(r) {
			for(var i = 0; i < user.userroles.length; i++){
				if(r.rolename == user.userroles[i].rolename)
					return false;
			}
			return true;
		});
		
		
		// Figure out which roles to remove
		var rolesRemoved = user.userroles.filter(function(r) {
			for(var i = 0; i < userToEdit.userroles.length; i++){
				if(r.rolename == userToEdit.userroles[i].rolename)
					return false;
			}
			return true;
		});
		

		var dataString = "user=";
		dataString += userToEdit.username;
		
		if(rolesAdded.length > 0)
		{
			dataString += "&add=";
			for(var i = 0; i < rolesAdded.length; i++){
				dataString+= rolesAdded[i].rolename;
				if(i < rolesAdded.length - 1)
					dataString += ",";
			}
		}
		
		if(rolesRemoved.length > 0){
			dataString += "&remove=";
			for(var i = 0; i < rolesRemoved.length; i++){
				dataString+= rolesRemoved[i].rolename;
				if(i < rolesRemoved.length - 1)
					dataString += ",";
			}
		}
		
		
		var saveRequest = new XMLHttpRequest();
		saveRequest.onreadystatechange = function() {
			if(saveRequest.readyState == XMLHttpRequest.DONE && saveRequest.status == 200) {
				// Update new roles to user in table and clear
				for(var i = 0; i < data.users.length; i++)
					if(data.users[i].userid == userToEdit.userid){
						data.users[i] = userToEdit;
						break;
					}
				
				fillUserTable(data.users);
				clearEditBox();
			}
		}
		saveRequest.open("POST", "/ApplicationDirectoryBasic/role");
		saveRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		saveRequest.send(dataString);
		
	}
}

function compareRoles(first, second) {
	if(first.roleid  < second.roleid)
		return -1;
	else if(second.roleid < first.roleid)
		return 1;
	
	return 0;
	
}

function hasRoleChanges(first, second) {
	if(first.length != second.length)
		return true;
	
	for(var i = 0; i < first.length; i++)
		if(first[i].roleid != second[i].roleid)
			return true;
	
	return false;
}

function getRolesNotSet(roles) {
	var uRoles = roles.filter(function (r) {
		for(var i = 0; i < userToEdit.userroles.length; i++){
			if(r.rolename == userToEdit.userroles[i].rolename)
				return false;
		}
		return true;
	});
	
	return uRoles;
}

function fillSelect(options) {
	
	var select = document.getElementById("selectMod");
	
	var selectString = "";
	options.forEach(function(r) {
		selectString += "<option>" + r.rolename + "</option>";
		
	});
	
	select.innerHTML = selectString;
}

function fillRolesTable(roles) {
	var bodyString = "";
	
	roles.forEach(function(r) {
		bodyString += "<tr id='" + r.rolename + "Row'><td>" + r.rolename + "</td><td><button onclick='removeRole(\"" + r.rolename + "\")'>Remove</button></td></tr>";
	});
	document.getElementById("modTableBody").innerHTML = bodyString;
}

function fillUserTable(users){
	var tBody = document.getElementById("rolesTableBody");
	var bodyString = "";
	users.forEach(function(data) {
		bodyString += "<tr id=\"" + data.username + "Row\">";
		bodyString += "<td>" + data.username + "</td>";
		bodyString += "<td>";
		if(data.userroles != null)
			for(var i = 0; i < data.userroles.length; i++){
				bodyString += data.userroles[i].rolename;
				
				if(i < data.userroles.length - 1)
					bodyString += ", ";
			}
		else
			data.userroles = [];
		bodyString += "</td>";
		bodyString += "<td id='" + data.username+ "edit' class='center'><button onclick=\"editRoles('" + data.username + "Row')\">Edit</button></td></tr>";
	});
	tBody.innerHTML = bodyString;
}

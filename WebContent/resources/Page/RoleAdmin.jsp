<script type="text/javascript" src="/ApplicationDirectoryBasic/resources/JS/RoleAdmin.js">
</script>
<h1 class="noTopMargin">Role Administrator</h1>

<table id="rolesTable">
	<thead>
		<tr><th class="percent20">Username</th><th class="percent60">Roles</th><th class="percent20">Edit</th></tr>
	</thead>
	<tbody id="rolesTableBody"></tbody>
</table>


<%-- Create Bottom box with change information --%>

<div id="modBox">
	<span>Username: <span id="usernameMod"></span></span>
	<table id="modTable" class="center">
		<thead>
			<tr><th>Role</th><th>Remove</th></tr>
		</thead>
		<tbody id="modTableBody"></tbody>
	</table>
	<div id="selectBlock">
		<select id="selectMod" disabled></select>
		<button id="addRoleButton" onclick="addRole()" disabled>Add</button>
	</div>
	<div class="right">
		<button id="save" onclick="save()" disabled="disabled">Save</button>
		<button id="cancel" onclick="clearEditBox()" disabled="disabled">Cancel</button>
	</div>
</div>
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="/ApplicationDirectoryBasic/resources/JS/CompUser.js">
</script>

<h1 class="noTopMargin">Company User</h1>

<div id="compBlock">
	<table id="compTable">
		<tbody>
			<tr>
				<td>Name:</td>
				<td id="nameCell">${sessionScope.UserCred.company.name}</td>
				<td>City:</td>
				<td id="cityCell">${sessionScope.UserCred.company.city}</td>
			</tr>
			<tr>
				<td>State:</td>
				<td id="stateCell">${sessionScope.UserCred.company.stateCode[0]}${sessionScope.UserCred.company.stateCode[1]}</td>
				<td>Contacts:</td>
				<td></td>
			</tr>
		</tbody>
	</table>
	<div class="right">
		<button id="cancel" onclick="cancelChanges()" disabled>Cancel</button>
		<button id="save" onclick="saveChanges()" disabled>Save</button>
		<c:if test="${empty sessionScope.UserCred.company }"><button id="createCom" onclick="createCompany()">Create</button></c:if>
		<c:if test="${not empty sessionScope.UserCred.company }"><button id="editCom" onclick="editCompany()">Edit</button></c:if>
	</div>
</div>

<c:if test="${not empty sessionScope.UserCred.company}">
	<table id="compJobTable">
		<thead>
			<tr>
				<th>Job Title</th>
				<th>Status</th>
				<th>View</th>
			</tr>
		</thead>
	</table>
</c:if>
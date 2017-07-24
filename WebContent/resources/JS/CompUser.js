var stateCodes = [ "", "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS",
                   "KY", "LA", "MA", "ME", "MD", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NY",
                   "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY" ];
var savedName =  null;
var savedCity = null;
var savedState = null;
var savedConName = null;
var savedConPhone = null;
var savedConMail = null;

function createCompany(){
	fillCompanytable();
	document.getElementById("createCom").disabled = true;
	document.getElementById("cancel").disabled = false;
	document.getElementById("save").disabled = false;
}

function editCompany(){
	fillCompanytable(document.getElementById("nameCell").textContent, document.getElementById("cityCell").textContent, document.getElementById("stateCell").textContent,
			         document.getElementById("cName").textContent, document.getElementById("cPhone").textContent, document.getElementById("cMail").textContent);
	document.getElementById("editCom").disabled = true;
	document.getElementById("cancel").disabled = false;
	document.getElementById("save").disabled = false;
}

function fillCompanytable(name, city, state, conName, conPhone, conMail){
	var nameIn = document.createElement("input");
	var cityIn = document.createElement("input");
	var stateSelect = document.createElement("select");
	var conNameIn = document.createElement("input");
	var conPhoneIn = document.createElement("input");
	var conMailIn = document.createElement("input");
	
	nameIn.id = "name";
	cityIn.id = "city";
	stateSelect.id = "state";
	conNameIn.id = "conName";
	conPhoneIn.id = "conPhone";
	conMailIn.id = "conMail";
	
	stateCodes.forEach(function(elt) {
		var op = document.createElement("option");
		op.text = elt;
		op.value = elt;
		stateSelect.options.add(op);
	});
	
	if(arguments.length !== 0){
		fillTableInfo("", "", "", "", "", "");
		
		nameIn.value = name;
		cityIn.value = city;
		stateSelect.value = state;
		conNameIn.value = conName;
		conPhoneIn.value = conPhone;
		conMailIn.value = conMail;
		
		/*for(var i = 0; i < stateSelect.options.length; i++){
			
			if(stateSelect.options[i].value === state){
				stateSelect.selectedIndex = i;
				break;
			}
		}*/
		
		savedName = name;
		savedCity = city;
		savedState = state;
		savedConName = conName;
		savedConPhone = conPhone;
		savedConMail = conMail;
	}
	
	document.getElementById("nameCell").appendChild(nameIn);
	document.getElementById("cityCell").appendChild(cityIn);
	document.getElementById("stateCell").appendChild(stateSelect);
	document.getElementById("cName").appendChild(conNameIn);
	document.getElementById("cPhone").appendChild(conPhoneIn);
	document.getElementById("cMail").appendChild(conMailIn);
}

function cancelChanges()
{
	if(savedName !== null && savedCity !== null && savedState !== null
	   && savedConName !== null && savedConPhone !== null && savedConMail !== null){
		fillTableInfo(savedName, savedCity, savedState, savedConName, savedConPhone, savedConMail);
		document.getElementById("editCom").disabled = false;
	}
	else if(savedName == null && savedCity == null && savedState == null
			   && savedConName == null && savedConPhone == null && savedConMail == null){
		fillTableInfo("", "", "", "", "", "");
		document.getElementById("createCom").disabled = false;
	}
	document.getElementById("cancel").disabled = true;
	document.getElementById("save").disabled = true;
}

function fillTableInfo(name, city, state, conName, conPhone, conMail) {
	document.getElementById("nameCell").innerHTML = name;
	document.getElementById("cityCell").innerHTML = city;
	document.getElementById("stateCell").innerHTML = state;
	document.getElementById("cName").innerHTML = conName;
	document.getElementById("cPhone").innerHTML = conPhone;
	document.getElementById("cMail").innerHTML = conMail;
}


function saveChanges(){
	if(document.getElementById("name").value !== "" && document.getElementById("city").value !== "" && document.getElementById("state").value !== ""
		&& document.getElementById("conName").value !== "" && document.getElementById("conPhone").value !== "" && document.getElementById("conMail").value !== ""){
		var saveRequest = new XMLHttpRequest();
		saveRequest.onreadystatechange = function (){
			if(saveRequest.readyState == XMLHttpRequest.DONE && saveRequest.status == 200){
				// Place new data in cells and remove inputs
				fillTableInfo(document.getElementById("name").value, document.getElementById("city").value, document.getElementById("state").value,
						      document.getElementById("conName").value, document.getElementById("conPhone").value, document.getElementById("conMail").value);
				
				// If just freshly created, turn the create button into an edit button
				var create = document.getElementById("createCom");
				if(create !== null){
					create.onclick = editCompany;
					create.innerHTML = "Edit";
					create.id = "editCom";
					create.disabled = false;
					
					var jobtable = document.createElement("table");
					jobtable.id = "compJobTable";
					jobtable.innerHTML = '<thead><tr><th>Job Title</th><th>Status</th><th>View</th></tr></thead>';
					
					document.getElementById("compBlock").parentElement.appendChild(jobtable);
				}
				else
					document.getElementById("editCom").disabled = false;
				
				var resp = JSON.parse(saveRequest.responseText);
				
				document.getElementById("comId").value = resp.com;
				document.getElementById("contId").value = resp.cont;
				
				
				// Disable cancel and save buttons
				document.getElementById("cancel").disabled = true;
				document.getElementById("save").disabled = true;
			}
		};
		
		var requestString = "comId=" + document.getElementById("comId").value + "&name=" + document.getElementById("name").value + "&city=" + document.getElementById("city").value + "&state=" + document.getElementById("state").value
	       + "&contId=" + document.getElementById("contId").value + "&conName=" + document.getElementById("conName").value + "&conPhone=" + document.getElementById("conPhone").value + "&conMail=" + document.getElementById("conMail").value
		
		saveRequest.open("POST", "/ApplicationDirectoryBasic/company");
		saveRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		saveRequest.send(requestString);
	}
}
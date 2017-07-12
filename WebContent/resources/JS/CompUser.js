var stateCodes = [ "", "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS",
                   "KY", "LA", "MA", "ME", "MD", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NY",
                   "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY" ];
var savedName =  null;
var savedCity = null;
var savedState = null;

function createCompany(){
	fillCompanytable();
	document.getElementById("createCom").disabled = true;
	document.getElementById("cancel").disabled = false;
	document.getElementById("save").disabled = false;
}

function editCompany(){
	fillCompanytable(document.getElementById("nameCell").textContent, document.getElementById("cityCell").textContent, document.getElementById("stateCell").textContent);
	document.getElementById("editCom").disabled = true;
	document.getElementById("cancel").disabled = false;
	document.getElementById("save").disabled = false;
}

function fillCompanytable(name, city, state){
	var nameIn = document.createElement("input");
	var cityIn = document.createElement("input");
	var stateSelect = document.createElement("select");
	
	nameIn.id = "name";
	cityIn.id = "city";
	stateSelect.id = "state";
	
	stateCodes.forEach(function(elt) {
		var op = document.createElement("option");
		op.text = elt;
		op.value = elt;
		stateSelect.options.add(op);
	});
	
	if(arguments.length !== 0){
		document.getElementById("nameCell").innerHTML = "";
		document.getElementById("cityCell").innerHTML = "";
		document.getElementById("stateCell").innerHTML = "";
		
		nameIn.value = name;
		cityIn.value = city;
		stateSelect.value = state;
		
		/*for(var i = 0; i < stateSelect.options.length; i++){
			
			if(stateSelect.options[i].value === state){
				stateSelect.selectedIndex = i;
				break;
			}
		}*/
		
		savedName = name;
		savedCity = city;
		savedState = state;
	}
	
	document.getElementById("nameCell").appendChild(nameIn);
	document.getElementById("cityCell").appendChild(cityIn);
	document.getElementById("stateCell").appendChild(stateSelect);
}

function cancelChanges()
{
	if(savedName !== null && savedCity !== null && savedState !== null){
		document.getElementById("nameCell").innerHTML = savedName;
		document.getElementById("cityCell").innerHTML = savedCity;
		document.getElementById("stateCell").innerHTML = savedState;
		document.getElementById("editCom").disabled = false;
	}
	else if(savedName == null && savedCity == null && savedState == null){
		document.getElementById("nameCell").innerHTML = "";
		document.getElementById("cityCell").innerHTML = "";
		document.getElementById("stateCell").innerHTML = "";
		document.getElementById("createCom").disabled = false;
	}
	document.getElementById("cancel").disabled = true;
	document.getElementById("save").disabled = true;
}

function saveChanges(){
	if(document.getElementById("name").value !== "" && document.getElementById("city").value !== "" && document.getElementById("state").value !== ""){
		var saveRequest = new XMLHttpRequest();
		saveRequest.onreadystatechange = function (){
			if(saveRequest.readyState == XMLHttpRequest.DONE && saveRequest.status == 200){
				// Place new data in cells and remove inputs
				document.getElementById("nameCell").innerHTML = document.getElementById("name").value;
				document.getElementById("cityCell").innerHTML = document.getElementById("city").value;
				document.getElementById("stateCell").innerHTML = document.getElementById("state").value;
				
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
				
				
				// Disable cancel and save buttons
				document.getElementById("cancel").disabled = true;
				document.getElementById("save").disabled = true;
			}
		};
		
		saveRequest.open("POST", "/ApplicationDirectoryBasic/company");
		saveRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		saveRequest.send("name=" + document.getElementById("name").value + "&city=" + document.getElementById("city").value + "&state=" +document.getElementById("state").value);
	}
}
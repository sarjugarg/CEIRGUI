package org.gl.ceir.datatable.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gl.ceir.Class.HeadersTitle.DatatableHeaderModel;
import org.gl.ceir.configuration.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatatableHeaders {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	Translator Translator;
	@PostMapping("headers")
	public ResponseEntity<?> headers(@RequestParam(name="type",defaultValue = "stock",required = false) String role,
			@RequestParam(name = "action", required = false) String Operation){
		List<DatatableHeaderModel> dataTableInputs = new ArrayList<>();
		try {

			// CONSIGNMENT DATATABLE HEADERS			
			if("consignment".equals(role)) {
				String[] headers = {"table.creationDate","table.transactionID","table.suppliername","table.status","table.taxPaidStatus","input.quantity","input.deviceQty","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}


		

			//STOLEN DATATABLE HEADERS
			else if("stolen".equals(role)) {
				String[] headers = {"table.date","table.transactionID","table.fileName","table.status","table.source","table.requestType","table.quantity","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}


			//CUSTOM DATATABLE HEADERS
			else if("customConsignment".equals(role)) {
				String[] headers = {"table.creationDate","table.transactionID","table.importerCompanyName","table.status","table.taxPaidStatus","input.quantity","input.deviceQty","table.action"};		
				for(String header : headers) {
					log.info("Translator.toLocale(header)----"+Translator.toLocale(header));
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}


			//Admin DATATABLE HEADERS
			else if("adminConsignment".equals(role)) {
				String[] headers = {"table.creationDate","table.transactionID","table.importerCompanyName","table.status","table.taxPaidStatus","input.quantity","input.deviceQty","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}

			//consignments from stolen headers
			else if("stolenconsignment".equals(role)) {
				String[] headers = {"table.blankheader","table.requestdate","table.status","table.suppliername","table.consignmentStatus","table.taxPaidStatus","table.quantity"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}

			//stock headers
			else if("stockcheckHeaders".equals(role)) {
				String[] headers = {"table.blankheader","table.requestdate","table.transactionID","table.fileName","table.status","table.quantity"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}

			//stockHeaders
			else if("stockHeaders".equals(role)) {
				
				String[] headers = {"table.creationDate","table.transactionID","table.fileName","table.status","input.quantity","input.devicequantity","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}

			//customStock Headers
			else if("customStockHeaders".equals(role)) {
				String[] headers = {"table.creationDate","table.assignto","table.transactionID","table.fileName","table.status","input.quantity","input.devicequantity","table.action"};	
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}


			//AdminStock Headers
			else if("adminStockHeaders".equals(role)) {
				String[] headers = {"table.creationDate","table.transactionID","table.importerCompanyName","table.roleType","table.fileName","table.status","input.quantity","input.devicequantity","table.action"};	
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}
			
			
			
			//stolen headers
			else if("stolenCheckHeaders".equals(role)) {
				String[] headers = {"table.blankheader","table.requestdate","table.transactionID","table.fileName","table.status","table.source","table.requestType"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}


			//Grievance Headers
			else if("grievanceHeaders".equals(role)) {
				
				String[] headers = {"table.creationDate","table.lastupdatedate","table.transactionID","table.grievanceID","table.status","table.action"};	
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}


			

			//AdminRegistration Headers
			else if("adminRegistration".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.UserName","table.email","table.phone","table.AsType","table.userType","table.status","table.action"};	
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}

			//DashBoard dataTable Headers
			else if("dashboardNotification".equals(role)) {
				String[] headers = {"table.date","table.transactionID","table.feature","table.message","table.action"};	
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}

			//TRC Manage Type dataTable Headers
			else if("trcManageType".equals(role)) {
				String[] headers = {"table.creationDate","table.transactionID","table.requestdate","table.ManufacturerName","table.country","table.TAC","table.status","table.Approve/RejectionDate","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}

			//DEFAULT PORTION  
			else if("userPaidStatus".equals(role)) {
				String[] headers = {"table.date","table.nid","table.transactionID","input.Nationality","table.taxPaidStatus","table.origin","table.status","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}



			//operator view
			else if("greyBlackList".equals(role)) {
				String[] headers = {"table.creationDate","table.fileName","table.fileType","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}

			
			//adminUserPaidStatus Headers 
			else if("adminUserPaidStatus".equals(role)) {

				String[] headers = {"table.date","table.nid","table.transactionID","input.Nationality","table.taxPaidStatus","table.origin","table.status","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			

			//adminUserPaidStatus Headers 
			else if("blockUnblock".equals(role)) {
				String[] headers = {"table.date","table.transactionID","table.requestType","input.mode","table.status","input.quantity","input.devicequantity","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			//adminSystemMessage Headers 
			else if("adminSystemMessage".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.feature","table.subject","table.Description","table.Value","table.Channel","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			//adminConfigMessage Headers 
			
			else if("adminConfigMessage".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.Description","table.Value","table.Type","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			
			//adminPolicyManagement Headers 
			
			else if("adminPolicyManagement".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.Description","table.Value","table.Type","table.Period","table.status","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			//AdmintrcManageType Headers 
			
			else if("AdmintrcManageType".equals(role)) {
				String[] headers = {"table.creationDate","table.transactionID","table.requestdate","table.ManufacturerName","table.country","table.TAC","table.status","table.Approve/RejectionDate","table.CEIRAdminStatus","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
//BlockUnblockCEIRAdmin Headers 
			

			else if("BlockUnblockCEIRAdmin".equals(role)) {
				String[] headers = {"table.creationDate","table.transactionID","table.Operator","table.requestType","table.Mode","table.status","input.quantity","input.devicequantity","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
		
			
//lawfulStolenHeaders Headers 
			
			else if("lawfulStolenHeaders".equals(role)) {
				String[] headers = {"table.creationDate","table.transactionID","table.BlockType","table.requestType","table.Mode","table.status","input.quantity","input.devicequantity","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
//auditManagement Headers 
			
			else if("auditManagement".equals(role)) {
				//String[] headers = {"table.creationDate","table.transactionID","table.UserName","table.userType","table.roleType","table.feature","table.SubFeature","table.action"};
				String[] headers = {"table.creationDate","table.transactionID","table.UserName","table.userType","table.roleType","table.feature","table.SubFeature","table.publicIp","table.browser","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
//ManageUserType Headers 
			
			else if("ManageUserType".equals(role)) {
				String[] headers = {"table.RegisterDate","table.transactionID","input.nidInput","table.Nationality","table.LocalContactNumber","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			//ManageUserType Headers 
			
			else if("ImporterTrcManageType".equals(role)) {
				String[] headers = {"table.creationDate","table.Trademark","table.transactionID","table.TAC","table.ProductName","table.ModelNumber","table.country","table.status","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			//deviceActivation Headers 
			
			else if("deviceActivation".equals(role)) {
				String[] headers = {"table.RegisterDate","table.transactionID","table.PassportNumber","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			
			//AdminImprtertrcManageType Headers 
			
			else if("AdminImportertrcManageType".equals(role)) {
		//		String[] headers = {"table.creationDate","table.lastupdatedate","table.transactionID","table.displayName","table.TAC","table.ProductName","table.ModelNumber","table.country","table.userType","table.status","table.action"};		
				String[] headers = {"table.creationDate","table.lastupdatedate","table.transactionID","table.displayName","table.TAC","table.ProductName","table.ModelNumber","table.country","table.userType","table.status","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			
			//AssigneeStock
			
			else if("AssigneeStock".equals(role)) {
				String[] headers = {"tabel.Assignee","tabel.AssigneeName","table.Contact","table.email","table.action"};		
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			
//fieldManagement
			
			else if("fieldManagement".equals(role) && "viewAll".equals(Operation)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","tabel.field","table.displayName","tabel.fieldId","table.Description"};			
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			else if("fieldManagement".equals(role) && "filter".equals(Operation)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","tabel.field","table.displayName","tabel.fieldId","table.Description","table.action"};			
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			//Port Management
			
			else if("portManagement".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.address","table.port","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			//Currency Management
			
			else if("currencyHeaders".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.month","table.year","table.currency","table.cambodian","table.doller","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			//customer Care DashBoard dataTable Headers
			else if("ccdashboardNotification".equals(role)) {
				String[] headers = {"table.date","table.transactionID","table.feature","table.message","table.action"};	
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}
			
			//user Management
			
			else if("userManagementHeaders".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.userType","table.status","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			//user Feature Management
			
			else if("userFeatureHeaders".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.userType","table.feature","table.period","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}

//Rule List
			
			else if("ruleList".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.name","table.Description","table.status","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
//Rule feature Mapping
			
		
			else if("ruleFeatureMapping".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.ruleName","table.featureName","table.userType","table.order","table.gracePeriod","table.postGracePeriod","table.moveToGracePeriod","table.moveToPostGracePeriod","table.expectedOutput","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}


			
			//alert Management
			
			else if("alertManagementHeaders".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.alertId","table.featureName","table.Description","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			//Running alert Management
			
			else if("runningAlertManagementHeaders".equals(role)) {
				String[] headers = {"table.creationDate","table.alertId","table.Description","table.status"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			
			//IP Log Management
			
			else if("ipLogManagementHeaders".equals(role)) {
				//String[] headers = {"table.creationDate","table.UserName","table.publicIp","table.browser","table.userAgent"};
				String[] headers = {"table.creationDate","table.UserName","table.publicIp","table.browser"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			//Pending TAC List
			else if("pendingTACHeaders".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.transactionID","table.TAC","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			
			//Grievance Admin Headers
			else if("adminGrievanceHeaders".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.transactionID","table.grievanceID","table.UserName","table.raisedBy","table.userType","table.status","table.action"};
				//String[] headers = {"table.creationDate","table.lastupdatedate","table.transactionID","table.grievanceID","table.UserName","table.userType","table.raisedBy","table.status","table.action"};	
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}
			
			//SLA Table List
			
			else if("slaTableHeaders".equals(role)) {
				String[] headers = {"table.creationDate","table.UserName","table.transactionID","table.userType","table.featureName","table.status"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			
			//User Management
			
			else if("userTableHeaders".equals(role)) {
				String[] headers = {"table.creationDate","table.lastupdatedate","table.UserName","table.email","table.phone","table.userType","table.action"};
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);
			}
			
			//Visa Headers
			else if("adminVisaHeaders".equals(role)) {
				
				String[] headers = {"table.creationDate","table.lastupdatedate","table.transactionID","table.PassportNumber","table.visaType","input.VisaNumber","table.fileName","table.visaExpiry", "table.status","table.action"};	
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}
			
			//Address Mgmt Headers
			else if("systemAddressHeaders".equals(role)) {
				
				String[] headers = {"Created On","table.lastupdatedate","Province","District","Commune","Village","table.action"};	
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}
			
			//Schedule Headers
			else if("scheduleHeaders".equals(role)) {
				
				String[] headers = {"table.creationDate","table.lastupdatedate","sidebar.ReportCatagory","table.ReportName","input.email","table.action"};	
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.OK);	
			}

			//DEFAULT PORTION  
			else {
				String[] headers = {"table.date","table.transactionID","table.fileName","table.status","table.action"};    
				for(String header : headers) {
					dataTableInputs.add(new DatatableHeaderModel(Translator.toLocale(header)));
				}
				return new ResponseEntity<>(dataTableInputs, HttpStatus.NOT_FOUND);
			}


		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); 
		}


	}
}

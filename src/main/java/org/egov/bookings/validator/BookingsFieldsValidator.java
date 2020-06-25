package org.egov.bookings.validator;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class BookingsFieldsValidator {

	public void validateTenantId(String tenantId) {
		if(tenantId == null || tenantId == "")
		 throw new IllegalArgumentException("Invalid TenantId");
	}
	
	
	public void validateBusinessService(String businessService) {
		if(businessService == null || businessService == "")
			 throw new IllegalArgumentException("Invalid businessService");
	}
	
	public void validateAction(String action) {
		if(action == null || action == "")
			 throw new IllegalArgumentException("Invalid Action");
	}
	
	
	
	public static boolean isNullOrEmpty( final Object object )
	   {
	      if ( object == null )
	         return true;
	      if ( object instanceof String )
	         return ( ( String ) object ).length() == 0;
	      if ( object instanceof Collection )
	         return ( ( Collection< ? > ) object ).isEmpty();
	      if ( object instanceof Map )
	         return ( ( Map< ?, ? > ) object ).isEmpty();
	      if ( object.getClass().isArray() )
	      {
	         if( Array.getLength( object ) == 0 ) 
	         {
	            return true;
	         } 
	         else 
	         {
	            // test 1st dim array
	            for( int i = 0; i < Array.getLength( object ); i++ ) 
	            {
	               if( Array.get( object, i ) != null ) 
	               {
	                  // check if 2 dim array
	                  if( Array.get( object, i ).getClass().isArray() ) {
	                     if( Array.getLength( Array.get( object, i ) ) == 0 ) 
	                     {
	                        return true;
	                     }
	                     for( int j = 0; j < Array.getLength( Array.get( object, i ) ); j++ ) {
	                        if( Array.get( Array.get( object, j ), i ) != null ) 
	                        {
	                           // means found at least one data not null
	                           return false;
	                        }
	                     }
	                     // means all data of a row are null
	                     return true;
	                  } 
	                  else 
	                  {
	                     // means 1 dim array and one data not null
	                     return false;
	                  }
	               }
	            }
	            // all data are null
	            return true;
	         }
	      }
	      return false;
	   }
}

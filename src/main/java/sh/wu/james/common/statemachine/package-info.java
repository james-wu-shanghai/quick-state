/**
 * Generic State Machine Framework. 
 * <br>
 * Source in the package is for generate state machine. <p>
 * Currently, state machine is defined in a XML file and will be parsing by JAXB, 
 * then StateMachineFactory.java will convert it into a spring bean. and open initState(P payload) API 
 * to create State from a 'payload' class which has a 'status' field. The status field is for determine the 
 * state that status machine created.  
 * <br>
 * Client programmer could take following steps to take usage of the framework on State-Machine Creation:<p>
 * 1. Analysis business, make clear the 'States', 'Operations' and their transfer relationship.<p>
 * 2. Define a Java interface which represents all 'Operations'. It's recommended to create return 
 * interface itself for every method so one can use state-operation chain style when calling the 
 * methods in program.<p>
 * 3. Define a Java class which is the payload of state machine, and assign a status (Enum class are allowed 
 * only currently) field which will specified a state in the machine.<p>
 * 4. Configure XML files to setup the state machine, in the file, 'Support methods' of each state and 
 * event triggered by each method should aslo be defined.<P>
 * 5. Create listener Classes and Spring bean to handler the event, also, these listeners should be added to 
 * XML config file.<p>
 * 6. Call StateMachineFactory.initState(P payload) method to init a state which return the interface 
 * defined in step #1. Then state can be used.
 *       
 * @author James Wu
 * 
 */
package sh.wu.james.common.statemachine;
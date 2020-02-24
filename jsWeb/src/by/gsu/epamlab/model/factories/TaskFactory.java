package by.gsu.epamlab.model.factories;

import java.util.ResourceBundle;

import by.gsu.epamlab.exceptions.InitException;
import by.gsu.epamlab.ifaces.TaskDao;
import by.gsu.epamlab.model.impl.ServiceDb;
import by.gsu.epamlab.model.impl.TaskImplDb;
import by.gsu.epamlab.model.impl.TaskImplMemory;

public class TaskFactory {
	private static TaskDao taskImpl;

	private enum Sources {
		DB {
			@Override
			TaskDao getImpl(ResourceBundle rb) throws InitException {
				String dbName = rb.getString("db.name");
				String user = rb.getString("db.user");
				String password = rb.getString("db.password");
				ServiceDb.init(dbName, user, password);
				return new TaskImplDb();
			}
		},
		MEMORY {
			@Override
			TaskDao getImpl(ResourceBundle rb) throws InitException {
				return new TaskImplMemory();
			}
		};

		abstract TaskDao getImpl(ResourceBundle rb) throws InitException;
	}

	public static void init(ResourceBundle rb) throws InitException {
		String sourceType = rb.getString("source.task");
		Sources source = Sources.valueOf(sourceType.toUpperCase());
		taskImpl = source.getImpl(rb);
	}

	public static TaskDao getClassFromFactory() {
		return taskImpl;
	}
}
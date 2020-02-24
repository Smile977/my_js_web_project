package by.gsu.epamlab.model.factories;

import java.util.ResourceBundle;

import by.gsu.epamlab.exceptions.InitException;
import by.gsu.epamlab.ifaces.UserDao;
import by.gsu.epamlab.model.impl.ServiceDb;
import by.gsu.epamlab.model.impl.UserImplDb;
import by.gsu.epamlab.model.impl.UserImplMemory;

public class UserFactory {
	private static UserDao userImpl;

	private enum Sources {
		DB {
			@Override
			UserDao getImpl(ResourceBundle rb) throws InitException {
				String dbName = rb.getString("db.name");
				String user = rb.getString("db.user");
				String password = rb.getString("db.password");
				ServiceDb.init(dbName, user, password);
				return new UserImplDb();
			}
		},
		MEMORY {
			@Override
			UserDao getImpl(ResourceBundle rb) throws InitException {
				return new UserImplMemory();
			}
		};

		abstract UserDao getImpl(ResourceBundle rb) throws InitException;
	}

	public static void init(ResourceBundle rb) throws InitException {
		String sourceType = rb.getString("source.user");
		Sources source = Sources.valueOf(sourceType.toUpperCase());
		userImpl = source.getImpl(rb);
	}

	public static UserDao getClassFromFactory() {
		return userImpl;
	}
}
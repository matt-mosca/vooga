package networking;

import java.net.Socket;

public class CollaborativeAuthoringServer extends AbstractServer {

	public CollaborativeAuthoringServer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getPort() {
		return Constants.COLLABORATIVE_AUTHORING_SERVER_PORT;
	}

	@Override
	protected AbstractServerHandler getServerHandler(Socket acceptSocket) {
		// TODO Auto-generated method stub
		return null;
	}

}

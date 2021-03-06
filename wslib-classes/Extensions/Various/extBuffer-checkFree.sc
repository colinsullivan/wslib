+ Buffer {

	// wslib 2008/2018
	// free a buffer and check if it is really freed.
	// Use this remote servers in high traffic situations to prevent new
	// buffers to be allocated when a "/b_free" message is still on the way

	checkFree { |action|

		OSCFunc({  |msg, time, addr, recvPort|
				this.freeMsg; // cleans up internally
				action.value( this );
		},  '/done', server.addr, nil, ['/b_free', bufnum ] ).oneShot;

		server.addr.sendMsg( "/b_free", bufnum );
	}

	checkCloseFree { |action|

		OSCFunc({  |msg, time, addr, recvPort|
				this.freeMsg; // cleans up internally
				action.value( this );
		},  '/done', server.addr, nil, ['/b_free', bufnum ] ).oneShot;

		server.addr.sendMsg( "b_close", bufnum, ["/b_free", bufnum ] );
	}

	/* // This method has flaws and I don't think it's used anywhere
	*checkFreeMulti { |buffers, action|

		if( 1.respondsTo( \factorial ) ) // *very* dirty check to see if this is > rev8139
			{
			// post rev8193 : less traffic

				OSCpathResponder( buffers[0].server.addr, [ '/done', '/b_free' ], {
					|time,resp,msg|
						var buf = buffers[msg[2]];
						buf.uncache;
						buf.server.bufferAllocator.free(buf.bufnum);
						action.value( buf );
						//resp.remove;
				 	}).add;


			buffers[0].server.addr.sendBundle( nil,
				*buffers.collect({ |buf| ["/b_free", buf.bufnum] })
				);
			};
			/*
			{
			// pre rev8193
			OSCpathResponder( server.addr, [ '/b_info', bufnum, 0 ], { |time,resp,msg|
				this.uncache;
				server.bufferAllocator.free(bufnum);
				action.value( this );
				resp.remove;
			 	}).add;

			server.addr.sendMsg( "/b_free", bufnum, ["/b_query", bufnum] );
			};
			*/

		}
		*/
	}
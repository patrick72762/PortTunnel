/*
MIT License
Copyright (c) 2019 Patrick72762
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package PortTunnelLib;

import java.net.Socket;

public class ClientClient extends Thread {
    PortTunnel main;

    public ClientClient(PortTunnel main){
        this.main = main;
    }

    public void run() {
        Log.Write(LogType.Info, "Starting ClientClient-Mode ...");
        Loop();
    }

    private void Loop()
    {
        try
        {
            Socket controlClient = new Socket(
                    main.Connection2.Host, main.ControlPort);

            sleep(2);

            controlClient.getOutputStream().write((byte)'c');

            sleep(2);

            while (main.Status == 1)
            {
                if (controlClient.getInputStream().available() > 0)
                {
                    if (controlClient.getInputStream().read()
                            == (byte)'n')
                    {
                        Socket cl1 = new Socket(
                                main.Connection2.Host, main.Connection2.Port);

                        Socket tunnelClient = new Socket(
                                main.Connection1.Host, main.Connection1.Port);

                        Tunnel tunnel = new Tunnel(cl1, tunnelClient, main);
                        tunnel.start();
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.Write(LogType.Error,
                    "Can't start PortTunnel");
        }
    }
}

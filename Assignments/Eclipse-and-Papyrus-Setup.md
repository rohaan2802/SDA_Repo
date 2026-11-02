# Eclipse and Papyrus Setup (for UML Diagrams)

Steps used to set up Eclipse with the Papyrus plug-in for drawing the course UML diagrams (use case, domain model, sequence and class diagrams).

## Step 1 – Install Eclipse IDE

1. Download **Eclipse IDE for Java Developers** (or the Java EE edition for enterprise apps) from <https://www.eclipse.org/downloads/>.
2. Install and launch Eclipse.
3. Use an up-to-date Eclipse release – Papyrus supports newer versions better.

## Step 2 – Install the Papyrus plug-in

1. In Eclipse open **Help → Install New Software…**
2. In **Work with**, enter the Papyrus update-site URL for your Eclipse version. The list of update sites is on <https://eclipse.dev/papyrus/download.html>.
3. Select **Papyrus for UML** (Papyrus for SysML, MARTE and others are optional).
4. Click **Next → Accept license → Finish**.
5. Restart Eclipse when asked.

## Step 3 – Create a Papyrus project

1. Go to **File → New → Other…**
2. Expand **Papyrus** and choose **Papyrus Project** or **Papyrus Model**.
3. Give the project a name (for example `MyUMLProject`).
4. Choose the model type – usually **UML**.
5. A `.uml` model file is created and the Papyrus perspective opens, ready for diagrams.

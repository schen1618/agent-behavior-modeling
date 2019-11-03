import javax.swing.*;

public abstract class Display extends JPanel
{
    Environment e;
    
    public Display(Environment e)
    {
        this.e = e;
    }
}

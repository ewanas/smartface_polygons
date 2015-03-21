package smartface.org.smartface_polygons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class polygons extends Activity {

  final String TAG = "polygons";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_polygons);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction()
              .add(R.id.container, new ProblemSpec())
              .commit();
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_polygons, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public void genProblem(View v) {
    Intent generate = new Intent(this, GenerateProblem.class);
    EditText points = (EditText)(findViewById(R.id.txtPointsNumber));
    EditText lines = (EditText)(findViewById(R.id.txtLinesNumber));

    generate.putExtra("points", points.getText().toString());
    generate.putExtra("lines", lines.getText().toString());
    startActivity(generate);
  }
}

package com.example.unscrambled

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unscrambled.ui.GameViewModel

@Composable
fun GameStatus(score: Int, modifier: Modifier = Modifier){ //card showing score
    Card (
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.score, score),
            style = typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameLayout(
    currentScrambledWord: String,
    isGuessWrong: Boolean,
    userGuess: String,
    onUserGuessChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
){ //Arrangement in game card
//    var name by remember { mutableStateOf("") }

    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ){
        Column(
//            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(mediumPadding)
        ) {
            Text(
                modifier = Modifier
                    .clip(shapes.medium)
                    .background(colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.End),
                text = stringResource(id = R.string.word_count, 0),
                style = typography.titleMedium,
                color = colorScheme.onPrimary
            )
            Text(
                text = currentScrambledWord,
                fontSize = 45.sp,
                modifier = modifier.align(Alignment.CenterHorizontally),
                style = typography.displayMedium
            )
            Text(
                text = stringResource(id = R.string.instructions),
                textAlign = TextAlign.Center,
                style = typography.titleMedium
            )
            OutlinedTextField(
                value = "" ,
                singleLine = true,
                shape = shapes.large,
                isError = isGuessWrong,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(containerColor = colorScheme.surface),
                onValueChange = {  },
                label = {
                    if(isGuessWrong){
                        Text(text = stringResource(id = R.string.wrong_guess))
                    }else{
                        Text(text = stringResource(id = R.string.enter_your_word))
                    }
                        },
                isError = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyboardDone() }
                )
            )

        }
    }
}

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel()
){ //Arrangement on the screen

    val mediumPadding = dimensionResource(id = R.dimen.padding_medium)
    val gameUiState by gameViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(mediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = typography.titleLarge
        )

        GameLayout(
            currentScrambledWord = gameUiState.currentScrambledWord,
            userGuess = gameViewModel.userGuess,
            onUserGuessChanged = { gameViewModel.updateUserGuess(it) },
            onKeyboardDone = { gameViewModel.checkUserGuess() },
            isGuessWrong = gameUiState.isGuessedWordWrong,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(mediumPadding)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding),
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                modifier = Modifier
                    .fillMaxWidth()
//                    .weight(1f)
//                    .padding(start = 8.dp)
                    ,
                onClick = { gameViewModel.checkUserGuess() }
            ){
                Text(
                    text = stringResource(id = R.string.submit),
                    fontSize = 16.sp
                )
            }

            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.skip),
                    fontSize = 16.sp
                )
            }
        }
        
        GameStatus(score = 0, modifier = Modifier.padding(20.dp))
    }
}

//final score dialog
@Composable
private fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,//composable parameter that takes any type and returns nothing
    modifier: Modifier = Modifier
){
    val activity = (LocalContext.current as Activity)//this allows you to manipulate the Lifecycle of the app 

    AlertDialog(
        onDismissRequest = {
            //Dismiss the dialog when the user clicks outside the dialog or on the button.
            //
        /*TODO*/
        },
        title = { Text(text = stringResource(id = R.string.congratulations))},
        text = { Text(text = stringResource(id = R.string.you_scored, score))},
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = { 
                    activity.finish()//this affects the lifecycle of the app 
               }
            ) {
                Text(text = stringResource(id = R.string.exit))
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = stringResource(id = R.string.play_again))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GamePre(){
//    GameStatus(score = 0)
//        GameLayout("apple")
        GameScreen()

}
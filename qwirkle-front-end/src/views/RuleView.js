import React, {Component} from 'react';

class RuleView extends Component{

    render() {
        return (
            <div>
                <div className='ruleBackground'>
                    <h1 className='ruleTitle'>Qwirkle Rules</h1>
                    <h1 className='ruleHeading'>Object</h1>
                    <p className='ruleText'>Make lines of tiles that are either all one color
                    or all one shape. Points are scored for every
                    tile played. The player with the most points
                    wins the game. 
                    </p>

                    <h1 className='ruleHeading'>Playing the Game</h1>
                    <p className='ruleText'>On your turn:</p>
                    <p className='ruleText'>1. Place one or more tiles.</p>
                    <p className='ruleText'>2. Tally your score.</p>
                    <p className='ruleText'>3. Draw tiles from the bag to bring your hand back up to six.</p>
                    <p className='ruleText'>
                        If you can't or don't want to place tiles, you may discard instead. This counts
                        as your entire turn and you do not score any points. Set aside all the tiles you
                        want to discard, then draw the same number of replacement tiles. You
                        cannot discard more tiles than are in the bag. After you have drawn, mix the
                        discarded tiles back into the bag. 
                    </p>

                    <h1 className='ruleHeading'>Placing Tiles</h1>
                    <p className='ruleText'>
                        You may play multiple tiles on your turn as long as all tiles played are the
                        same color or the same shape and are placed in the same line. You cannot
                        play two tiles that are exactly the same.
                        At least one of the tiles you play must touch (side to side) a tile that has
                        already been played and match the tile in color or shape. Any tiles that
                        touch each other are part of a line. Lines are either all one shape or all one
                        color, without any duplicates. 
                    </p>
                    <img className='ruleFigure1' src='/rule_images/rule_img_1.jpg' alt='img_1'/>
                    <p className='ruleText'>
                        The tiles you play must be added to the same line, but they do not have to 
                        touch each other. 
                    </p>
                    <img className='ruleFigure2' src='/rule_images/rule_img_2.jpg' alt='img_2'/>
                    <p className='ruleText'>
                        There cannot be duplicate tiles in a line. For example, a line of squares can 
                        only have one blue square. A line can never be longer than six tiles. 
                        As the game progresses, spaces will be created where no tile can be played.
                    </p>
                    <h1 className='ruleHeading'>Scoring</h1>
                    <p className='ruleText'>
                        One point is scored for each tile in a line that you create or add to. 
                    </p>
                    <img className='ruleFigure2' src='/rule_images/rule_img_3.jpg' alt='img_3'/>
                    <p className='ruleText'>A single tile can score two points if it is part of two lines.</p>
                    <img className='ruleFigure2' src='/rule_images/rule_img_4.jpg' alt='img_4'/>
                    <p className='ruleText'>
                        Whenever you complete a line of all six colors or shapes, it is called a 
                        Qwirkle. A Qwirkle scores 12 points, six for the tiles in the line plus six 
                        bonus points. 
                    </p>
                    <img src='/rule_images/rule_img_5.jpg' alt='img_5'/>
                    <p className='ruleText'>
                        A six-point bonus is scored by the first player to run out of tiles. 
                        For more scoring examples, refer to the Sample Game on the back of these 
                        instructions. 
                    </p>
                    <h1 className='ruleHeading'>Ending the Game</h1>
                    <p className='ruleText'>
                        When there are no more tiles to draw, play continues as before, but players 
                        do not replenish their hands. The first player to run out of tiles scores six 
                        bonus points and ends the game. The player with the highest score wins. 
                    </p>

                    
                </div>
                <button className='backButton' onClick={() => {this.props.backToStart()}}>Back</button>
            </div>
        );
    }
}
export default RuleView
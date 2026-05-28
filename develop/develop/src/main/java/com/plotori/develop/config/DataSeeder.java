package com.plotori.develop.config;

import com.plotori.develop.domain.entity.Text;
import com.plotori.develop.repository.TextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {
    @Bean
    @Profile("dev")
    CommandLineRunner seedTexts(TextRepository textRepository) {
        return args -> {
            if (textRepository.count() == 0) {
                // Othello
                Text othello = Text.builder()
                        .title("Othello")
                        .author("William Shakespeare")
                        .originalPassage("""
                        OTHELLO: Soft you; a word or two before you go.
                        I have done the state some service, and they know't.
                        No more of that. I pray you, in your letters,
                        When you shall these unlucky deeds relate,
                        Speak of me as I am; nothing extenuate,
                        Nor set down aught in malice: then must you speak
                        Of one that loved not wisely but too well;
                        Of one not easily jealous, but being wrought
                        Perplex'd in the extreme; of one whose hand,
                        Like the base Indian, threw a pearl away
                        Richer than all his tribe; of one whose subdued eyes,
                        Albeit unused to the melting mood,
                        Drop tears as fast as the Arabian trees
                        Their medicinal gum. Set you down this;
                        And say besides, that in Aleppo once,
                        Where a malignant and a turban'd Turk
                        Beat a Venetian and traduced the state,
                        I took by the throat the circumcised dog,
                        And smote him, thus.
                        [Stabs himself]
                        """)
                        .publicationYear(1603)
                        .genre("Tragedy")
                        .canonicalStatus("CANONICAL")
                        .build();

                // Jane Eyre
                Text janeEyre = Text.builder()
                        .title("Jane Eyre")
                        .author("Charlotte Brontë")
                        .originalPassage("""
                        Reader, I married him. A quiet wedding we had: he and I, the parson and clerk, 
                        were alone present. When we got back from church, I went into the kitchen of the 
                        manor-house, where Mary was cooking the dinner and John cleaning the knives, and I said —
                        
                        "Mary, I have been married to Mr. Rochester this morning." The housekeeper and her 
                        husband were both of that decent phlegmatic order of people, to whom one may at any 
                        time safely communicate a remarkable piece of news without incurring the danger of 
                        having one's ears pierced by some shrill ejaculation, and subsequently stunned by 
                        a torrent of wordy wonderment.
                        """)
                        .publicationYear(1847)
                        .genre("Gothic Romance")
                        .canonicalStatus("CANONICAL")
                        .build();

                // Macbeth
                Text macbeth = Text.builder()
                        .title("Macbeth")
                        .author("William Shakespeare")
                        .originalPassage("""
                        MACBETH: She should have died hereafter;
                        There would have been a time for such a word.
                        Tomorrow, and tomorrow, and tomorrow,
                        Creeps in this petty pace from day to day
                        To the last syllable of recorded time,
                        And all our yesterdays have lighted fools
                        The way to dusty death. Out, out, brief candle!
                        Life's but a walking shadow, a poor player
                        That struts and frets his hour upon the stage
                        And then is heard no more: it is a tale
                        Told by an idiot, full of sound and fury,
                        Signifying nothing.
                        """)
                        .publicationYear(1606)
                        .genre("Tragedy")
                        .canonicalStatus("CANONICAL")
                        .build();

                textRepository.save(othello);
                textRepository.save(janeEyre);
                textRepository.save(macbeth);

                System.out.println("✅ Seeded 3 canonical texts: Othello, Jane Eyre, Macbeth");
            }
        };
    }
}

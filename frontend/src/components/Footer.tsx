import React from 'react'
import 'scss/_footer.scss'

export function Footer(): JSX.Element {
  return (
    <footer className="footer">
      <div className="footer-column">
        © 2019 Archeologický ústav AV ČR, Brno, v. v. i.
      </div>
      <div className="footer-column">
        Archeologický ústav AV ČR, Brno, v. v. i., Čechyňská 363/19, 602 00 Brno
      </div>
      <div className="footer-column">Poslední aktualizace 12.9.2019</div>
      <div className="footer-column">Datum spuštění 6.9.2019</div>
      <div className="footer-column">
        Kontakt: <a href="mailto:teater@arub.cz">teater@arub.cz</a>
      </div>
    </footer>
  )
}

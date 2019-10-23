import React, { FC } from 'react'

const Ooops: FC<{ text: string; subtext?: string }> = ({
  text,
  subtext = ''
}) => (
  <div className="oops-wrapper">
    <div className="oops-container">
      <div className="oops">
        {text}
        {subtext && <small>{subtext}</small>}
      </div>
    </div>
  </div>
)

export function Ooops404(): JSX.Element {
  return (
    <Ooops
      text="Stránku se nepodařilo najít."
      subtext="Buď neexistuje nebo je momentálně nedostupná."
    />
  )
}

export function OoopsError(): JSX.Element {
  return <Ooops text="Nastala chyba :(" subtext="Zkuste prosím později." />
}

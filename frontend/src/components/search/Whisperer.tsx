import { FormResult } from './_types'
import React, { FC } from 'react'
import { Link } from 'react-router-dom'
import { getTranslatedSearchType } from 'utils/utilizeSearchTypes'
import { createURL } from '../../utils/createURL'
import { URL_DESCRIPTION } from '../../constants/URL'

export const Whisperer: FC<{ data: FormResult[]; hide?: () => void }> = ({
  data = [],
  hide
}): JSX.Element | null =>
  data.length ? (
    <div className="whisperer-container">
      <ul className="whisperer">
        {data.map(item => (
          <li key={item.url + item.searchType + item.name}>
            <Link
              to={createURL(URL_DESCRIPTION, item.url)}
              title={item.name}
              onClick={() => hide && hide()}
            >
              <span className="title">{item.name}</span>
              <span className="meta">
                {getTranslatedSearchType(item.searchType)}
              </span>
            </Link>
          </li>
        ))}
      </ul>
    </div>
  ) : null

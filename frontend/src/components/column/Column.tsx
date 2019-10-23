import React, { FC } from 'react'
import { Link } from 'react-router-dom'
import classNames from 'classnames'
import times from 'lodash/times'
import Color from 'color'
import { URL_DESCRIPTION } from 'constants/URL'
import { createURL } from 'utils/createURL'
import { ColumnItem } from './_types'

const ColumnResult: FC<ColumnItem> = ({ name, url, active, color, isLeaf }) => {
  const finalColor =
    color &&
    Color(color)
      .alpha(0.4)
      .lighten(0.3)

  return (
    <li>
      <Link
        to={createURL(URL_DESCRIPTION, url)}
        title={name}
        className={classNames({ active, colorized: !!finalColor, leaf: isLeaf })}
        style={{
          ...(finalColor && {
            backgroundImage: `linear-gradient(120deg, ${finalColor.string(
              3
            )} 0%, ${finalColor.lighten(0.5).string(3)} 100%)`
          })
        }}
      >
        {name}
      </Link>
    </li>
  )
}

function ColumnsLoading(): JSX.Element {
  return (
    <>
      {times(
        7,
        (index): JSX.Element => {
          return <li key={index} className="loading-background" />
        }
      )}
    </>
  )
}

export const Column: FC<{ data?: ColumnItem[]; loading?: boolean }> = ({
  data = [],
  loading
}) => (
  <ul className="column">
    {loading ? (
      <ColumnsLoading />
    ) : (
      data.map(item => <ColumnResult key={item.url} {...item} />)
    )}
  </ul>
)
